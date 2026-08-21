package network;

import KVStore.WAL.Operation;

public class Request {
    private RequestOperation operation;
    private String key;
    private String value;

    public Request(RequestOperation operation, String key) {
        this.operation = operation;
        this.key = key;
    }

    public Request(RequestOperation operation, String key, String value) {
        this.operation = operation;
        this.key = key;
        this.value = value;
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
}
