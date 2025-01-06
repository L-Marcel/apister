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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Request extends Node {
    private RequestType type = RequestType.GET;
    private final StringProperty url;
    private final StringProperty body;
    private ObservableList<HeaderEntry> header;
    private final ObjectProperty<Response> lastResponse;

    public Request() {
        super();
        url = new SimpleStringProperty("");
        body = new SimpleStringProperty("");
        lastResponse = new SimpleObjectProperty<Response>(null);
        header = FXCollections.observableArrayList();
    };

    public Request(String name) {
        super(name);
        url = new SimpleStringProperty("");
        body = new SimpleStringProperty("");
        lastResponse = new SimpleObjectProperty<Response>(null);
        header = FXCollections.observableArrayList();
    };

    public Request(
        String name,
        String url,
        String body,
        Response lastResponse
    ) {
        super(name);
        this.url = new SimpleStringProperty(url);
        this.body = new SimpleStringProperty(body);
        this.lastResponse = new SimpleObjectProperty<Response>(lastResponse);
        header = FXCollections.observableArrayList();
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getValue());
        out.writeUTF(this.type.name());
        out.writeUTF(this.url.get());
        out.writeUTF(this.body.get());
        out.writeInt(this.header.size());

        for(HeaderEntry entry : this.header) {
            out.writeUTF(entry.getKey());
            out.writeUTF(entry.getValue());
        };

        boolean value = (this.lastResponse != null) ? true : false;
        out.writeBoolean(value);
        if(value) out.writeObject(this.lastResponse.get());
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setValue(in.readUTF());
        this.type = RequestType.valueOf(in.readUTF());
        this.url.set(in.readUTF());
        this.body.set(in.readUTF());
        
        int size = in.readInt();
        for(int i = 0; i < size; i++) {
            String key = in.readUTF();
            String value = in.readUTF();
            this.header.add(new HeaderEntry(key, value));
        };
        
        boolean hasLastResponse = in.readBoolean();
        if(hasLastResponse) this.lastResponse.set((Response) in.readObject());
    };

    public Response request() throws IOException, InterruptedException {
        Instant requestedAt = Instant.now();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest
            .newBuilder()
            .uri(URI.create(this.url.get()));

        if(this.header != null) {
            try {
                for(HeaderEntry entry : this.header) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                };
            } catch (Exception e) {
                e.printStackTrace();
            };
        };

        switch(this.type) {
            case GET:
                requestBuilder.GET();
                break;
            case POST:
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(this.body.get()));
                break;
            case PUT:
                requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(this.body.get()));
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
            default:
                break;
        };

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        this.lastResponse.set(new Response(
            requestedAt, 
            this.url.get(), 
            response.body(), 
            StatusCode.fromCode(response.statusCode()),
            new HashMap<String, List<String>>(response.headers().map())
        ));

        return this.lastResponse.get();
    };

    public Node rename(String name) {
        if(!(this.getParent() instanceof Node)) return this;

        Node parent = (Node) this.getParent();
        if(parent != null && !parent.childExists(name) && !name.isBlank()) {
            parent.getChildren().remove(this);
            Request request = new Request(
                name,
                this.url.get(),
                this.body.get(),
                this.lastResponse.get()
            );

            request.headerProperty().setAll(this.header);
            parent.replace(this, request);
            return request;
        };

        return this;
    };

    public RequestType getType() {
        return this.type;
    };

    public void setType(RequestType type) {
        this.type = type;
    };

    public StringProperty urlProperty() {
        return this.url;
    };

    public StringProperty bodyProperty() {
        return this.body;
    };

    public ObservableList<HeaderEntry> headerProperty() {
        return this.header;
    };

    public ObjectProperty<Response> lastResponseProperty() {
        return this.lastResponse;
    };
};
