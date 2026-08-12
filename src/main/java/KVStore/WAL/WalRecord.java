package KVStore.WAL;

import java.util.Objects;

public class WalRecord {
    private Operation operation;
    private String key;
    private String value;

    public WalRecord(Operation operation, String key, String value) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WalRecord walRecord = (WalRecord) o;
        return operation == walRecord.operation && Objects.equals(key, walRecord.key) && Objects.equals(value, walRecord.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, key, value);
    }
}
