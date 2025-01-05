package app.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class Request extends Node {
    private RequestType type = RequestType.GET;
    private String url = "";
    private String body = "";
    private HashMap<String, String> headers;
    private Response lastResponse;

    public Request() {};
    public Request(String name) {
        super(name);
        headers = new HashMap<String, String>();
    };

    public Response request() throws IOException, InterruptedException {
        Instant requestedAt = Instant.now();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest
            .newBuilder()
            .uri(URI.create(this.url));

        if(this.headers != null) {
            this.headers.forEach(requestBuilder::header);
        };

        switch(this.type) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(this.body));
                break;
            case PUT:
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(this.body));
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
            default:
                break;
        };

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        this.lastResponse = new Response(
            requestedAt, 
            this.url, 
            response.body(), 
            StatusCode.fromCode(response.statusCode()),
            new HashMap<String, List<String>>(response.headers().map())
        );

        return this.lastResponse;
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getValue());
        out.writeUTF(this.type.name());
        out.writeUTF(this.url);
        out.writeUTF(this.body);
        out.writeInt(this.headers.size());

        for(String key : this.headers.keySet()) {
            out.writeUTF(key);
            out.writeUTF(this.headers.get(key));
        };

        boolean value = (this.lastResponse != null) ? true : false;
        out.writeBoolean(value);
        if(value) out.writeObject(this.lastResponse);
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setValue(in.readUTF());
        this.type = RequestType.valueOf(in.readUTF());
        this.url = in.readUTF();
        this.body = in.readUTF();
        
        int size = in.readInt();
        for(int i = 0; i < size; i++) {
            String key = in.readUTF();
            String value = in.readUTF();
            this.headers.put(key, value);
        };
        
        boolean hasLastResponse = in.readBoolean();
        if(hasLastResponse) this.lastResponse = (Response) in.readObject();
    };

    public RequestType getType() {
        return this.type;
    };

    public void setType(RequestType type) {
        this.type = type;
    };

    public String getUrl() {
        return this.url;
    };

    public void setUrl(String url) {
        this.url = url;
    };

    public String getBody() {
        return this.body;
    };

    public void setBody(String body) {
        this.body = body;
    };

    public HashMap<String, String> getHeaders() {
        return this.headers;
    };

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    };

    public Response getLastResponse() {
        return this.lastResponse;
    };

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    };
};
