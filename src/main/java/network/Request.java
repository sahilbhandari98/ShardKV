package network;

import KVStore.WAL.Operation;

import java.util.Optional;

public class Request {
    private RequestOperation operation;
    private String key;
    private String value;
    private boolean isReplicationRequest;

    public Request(RequestOperation operation, String key) {
        this.operation = operation;
        this.key = key;
        if(RequestOperation.RDELETE.equals(operation)) {
            this.isReplicationRequest = true;
        }
    }

    public Request(RequestOperation operation, String key, String value) {
        this.operation = operation;
        this.key = key;
        this.value = value;
        if(RequestOperation.RPUT.equals(operation) || RequestOperation.RDELETE.equals(operation)) {
            this.isReplicationRequest = true;
        }
    }

    public RequestOperation getOperation() {
        return operation;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isReplicationRequest() {
        return isReplicationRequest;
    }

    public Request getReplicationRequest() {
        if(RequestOperation.PUT.equals(this.getOperation())) {
            return new Request(RequestOperation.RPUT, this.key, this.value);
        } else if(RequestOperation.DELETE.equals(this.getOperation())) {
            return new Request(RequestOperation.RDELETE, this.key);
        }
       return null;
    }

    @Override
    public String toString() {
        return "Request{" +
                "operation=" + operation +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", isReplicationRequest=" + isReplicationRequest +
                '}';
    }
}
