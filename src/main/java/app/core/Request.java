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
    private HashMap<String, String> header;
    private Response lastResponse;

    public Request(String name) {
        super(name);
    };

    public Response request() {
        Instant requetedAt = Instant.now();

        // [TODO] Fetch a HTTP request using HttpClient and HttpRequest

        return new Response(requetedAt, null);
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

    public HashMap<String, String> getHeader() {
        return header;
    };

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    };

    public Response getLastResponse() {
        return lastResponse;
    };

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    };
};
