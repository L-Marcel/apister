package app.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;
import java.util.HashMap;

public class Request extends Node {
    private RequestType type = RequestType.GET;
    private String url;
    private String body;
    private HashMap<String, String> headers;
    private Response lastResponse;

    public Request(String name) {
        super(name);
    };

    public Response request() {
        Instant requetedAt = Instant.now();
        HashMap<String, String> headers = new HashMap<String, String>();

        // [TODO] Fetch a HTTP request using HttpClient, 
        // HttpRequest and HttpResponse<String>
        // [TIP] HttpResponse<String> is an interface and for some reason its implementation 
        // is private.

        return new Response(
            requetedAt, 
            this.url, 
            "", 
            StatusCode.OK,
            headers
        );
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // [TODO] Serialize requests. 
        // [TIP] Requests are leafs!
        // [TIP] You can use super here.
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // [TODO] Deserialize requests. 
        // [TIP] Requests are leafs!
        // [TIP] You can use super here.
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
