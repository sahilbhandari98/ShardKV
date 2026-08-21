package network;

import KVStore.WAL.Operation;

public class RequestParser {
    public static Request requestParser(String request) {
        String values[] = request.split("\\|", -1);
        if(values == null || values.length < 2) {
            throw new IllegalArgumentException("Invalid request");
        }

        RequestOperation operation = RequestOperation.valueOf(values[0]);

        return switch (operation) {
            case PUT -> {
                if(values.length != 3) {
                    throw new IllegalArgumentException("Invalid operation");
                }
                yield new Request(operation, values[1], values[2]);
            }
            case GET,DELETE -> {
                if(values.length != 2) {
                    throw new IllegalArgumentException("Invalid Operation");
                }
                yield new Request(operation, values[1]);
            }
        };
    }
}
