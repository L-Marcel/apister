package app.core;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class Response implements Serializable {
    private String message;
    private String url;
    private StatusCode statusCode;
    private HashMap<String, List<String>> headers;
    private Instant requestedAt;
    private Instant receivedAt;
    
    public Response(
        Instant requestedAt,
        String url, 
        String message,
        StatusCode statusCode,
        HashMap<String, List<String>> headers
    ) {
        this.requestedAt = requestedAt;
        this.url = url;
        this.message = message;
        this.statusCode = statusCode;
        this.headers = headers;
        this.receivedAt = Instant.now();
    };

    public String getMessage() {
        return message;
    };

    public void setMessage(String message) {
        this.message = message;
    };

    public String getUrl() {
        return url;
    };

    public void setUrl(String url) {
        this.url = url;
    };

    public StatusCode getStatusCode() {
        return statusCode;
    };

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    };

    public HashMap<String, List<String>> getHeaders() {
        return headers;
    };

    public void setHeaders(HashMap<String, List<String>> headers) {
        this.headers = headers;
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
