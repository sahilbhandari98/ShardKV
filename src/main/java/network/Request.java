package network;

import KVStore.WAL.Operation;

public class Request {
    private Operation operation;
    private String key;
    private String value;

    public Request(Operation operation, String key) {
        this.operation = operation;
        this.key = key;
    }

    public Request(Operation operation, String key, String value) {
        this.operation = operation;
        this.key = key;
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
