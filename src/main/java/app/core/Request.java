package app.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import app.utils.RequestUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Request extends Node {
    private final ObjectProperty<RequestType> type;
    private final StringProperty url;
    private final StringProperty body;
    private ObservableList<HeaderEntry> header;
    private final ObjectProperty<Response> lastResponse;

    public Request() {
        super();
        type = new SimpleObjectProperty<RequestType>(RequestType.GET);
        url = new SimpleStringProperty("");
        body = new SimpleStringProperty("");
        lastResponse = new SimpleObjectProperty<Response>(null);
        header = FXCollections.observableArrayList();
    };

    public Request(String name) {
        super(name);
        type = new SimpleObjectProperty<RequestType>(RequestType.GET);
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
        this.type = new SimpleObjectProperty<RequestType>(RequestType.GET);
        this.url = new SimpleStringProperty(url);
        this.body = new SimpleStringProperty(body);
        this.lastResponse = new SimpleObjectProperty<Response>(lastResponse);
        header = FXCollections.observableArrayList();
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getValue());
        out.writeUTF(this.type.get().name());
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
        this.type.set(RequestType.valueOf(in.readUTF()));
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

    public Response submit() throws IOException, InterruptedException {
        Instant requestedAt = Instant.now();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        try {
            requestBuilder.uri(URI.create(this.url.get()));
        } catch(IllegalArgumentException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "URL inválida!",
                RequestUtils.createHeader(StatusCode.BAD_REQUEST),
                StatusCode.BAD_REQUEST
            ));
    
            return this.lastResponse.get();
        } catch(Exception e) {
            e.printStackTrace();
        };

        try {
            for(HeaderEntry entry : this.header) {
                if(entry.getKey().isBlank() || entry.getValue().isBlank()) continue;
                requestBuilder.header(entry.getKey(), entry.getValue());
            };
        } catch(IllegalArgumentException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "Cabeçalho inválido!",
                RequestUtils.createHeader(StatusCode.BAD_REQUEST),
                StatusCode.BAD_REQUEST
            ));
    
            return this.lastResponse.get();
        } catch(Exception e) {
            e.printStackTrace();
        };

        switch(this.type.get()) {
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
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            StatusCode statusCode = StatusCode.fromCode(response.statusCode());
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(), 
                response.body(),
                RequestUtils.convertHttpHeader(
                    response.headers(),
                    statusCode
                ),
                statusCode
            ));
        } catch(IOException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "Conexão fechada abrutamente!",
                RequestUtils.createHeader(StatusCode.BAD_REQUEST),
                StatusCode.BAD_REQUEST
            ));
    
            return this.lastResponse.get();
        } catch(InterruptedException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "Operação interrompida!",
                RequestUtils.createHeader(StatusCode.BAD_REQUEST),
                StatusCode.BAD_REQUEST
            ));
    
            return this.lastResponse.get();
        } catch(IllegalArgumentException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "Erro inesperado ao montar requisição!",
                RequestUtils.createHeader(StatusCode.BAD_REQUEST),
                StatusCode.BAD_REQUEST
            ));
    
            return this.lastResponse.get();
        } catch(SecurityException e) {
            this.lastResponse.set(new Response(
                requestedAt, 
                this.url.get(),
                "Requisição bloqueada!",
                RequestUtils.createHeader(StatusCode.NOT_ACCEPTABLE),
                StatusCode.NOT_ACCEPTABLE
            ));
    
            return this.lastResponse.get();
        } catch(Exception e) {
            e.printStackTrace();
        };

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
        return this.type.get();
    };

    public ObjectProperty<RequestType> typeProperty() {
        return this.type;
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
