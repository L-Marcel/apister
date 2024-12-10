package app.core;

import java.net.http.HttpResponse;
import java.time.Instant;

public class Response {
    private HttpResponse<String> data; 
    private Instant requestedAt;
    private Instant receivedAt;
    
    public Response(Instant requestedAt, HttpResponse<String> data) {
        this.data = data;
        this.requestedAt = requestedAt;
        this.receivedAt = Instant.now();
    };

    public HttpResponse<String> getData() {
        return data;
    };

    public void setData(HttpResponse<String> data) {
        this.data = data;
    };

    public Instant getRequestedAt() {
        return requestedAt;
    };

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    };

    public Instant getReceivedAt() {
        return receivedAt;
    };

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    };
};
