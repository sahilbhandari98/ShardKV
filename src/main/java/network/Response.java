package network;

import java.util.Objects;

public class Response {

    public enum Status {
        SUCCESS, VALUE, ERROR;
    }

    String payload;
    String shard;
    Status status;

    public Response(Status status, String shard, String payload) {
        this.status = status;
        this.shard = shard;
        this.payload = payload;
    }
    public static Response success(String shard) {
        return new Response(Status.SUCCESS, shard, "operation successfull");
    }

    public static Response value(String shard, String value) {
        return new Response(Status.VALUE, shard, value);
    }

    public String getPayload() {
        return payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getShard() {
        return this.shard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(payload, response.payload) && Objects.equals(shard, response.shard) && status == response.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, shard, status);
    }
}
