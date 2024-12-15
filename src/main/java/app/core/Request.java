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

public class Request extends Node {
    private RequestType type = RequestType.GET;
    private String url;
    private String body = "";
    private HashMap<String, String> headers;
    private Response lastResponse;

    public Request(String name) {
        super(name);
    };

    // Fiquei em dúvida se eu tratava as exceptions aqui na função, ou deixo pra a função que chamar ela.
    // Acabei deixando a responsabilidade pra função que chama, mas posso mudar.
    public Response request() throws IOException, InterruptedException {
        Instant requestedAt = Instant.now();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .uri(URI.create(this.url));

        if (this.headers != null) {
            this.headers.forEach(requestBuilder::header);
        }

        switch (this.type) {
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
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Response(
            requestedAt, 
            this.url, 
            response.body(), 
            StatusCode.fromCode(response.statusCode()),
            this.headers
        );
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(getValue());
        out.writeUTF(type.name());
        out.writeUTF(url);
        out.writeUTF(body);
        
        // lastResponse pode ser null?? Caso sim, depois mudo essa parte.
        out.writeObject(lastResponse);
        out.writeInt(headers.size());

        for (String key : headers.keySet()) {
            out.writeUTF(key);
            out.writeUTF(headers.get(key));
        }
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setValue(in.readUTF());
        type = RequestType.valueOf(in.readUTF());
        url = in.readUTF();
        body = in.readUTF();
        lastResponse = (Response) in.readObject();
        
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readUTF();
            String value = in.readUTF();
            headers.put(key, value);
        }
    };

    public RequestType getType() {
        return type;
    };

    public void setType(RequestType type) {
        this.type = type;
    };

    public String getUrl() {
        return url;
    };

    public void setUrl(String url) {
        this.url = url;
    };

    public String getBody() {
        return body;
    };

    public void setBody(String body) {
        this.body = body;
    };

    public HashMap<String, String> getHeaders() {
        return headers;
    };

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    };

    public Response getLastResponse() {
        return lastResponse;
    };

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    };
};
