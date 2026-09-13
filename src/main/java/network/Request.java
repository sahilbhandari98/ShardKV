package network;

import KVStore.WAL.Operation;

public class Request {
    private RequestOperation operation;
    private String key;
    private String value;
    private boolean isReplicationRequest;

    public Request(RequestOperation operation, String key) {
        this.operation = operation;
        this.key = key;
        this.isReplicationRequest = false;
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

    public void setReplicationOperation() {
        if(RequestOperation.PUT.equals(this.getOperation())) {
            this.operation = RequestOperation.RPUT;
            this.isReplicationRequest = true;
        } else if(RequestOperation.DELETE.equals(this.getOperation())) {
            this.operation = RequestOperation.RDELETE;
            this.isReplicationRequest = true;
        }
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
