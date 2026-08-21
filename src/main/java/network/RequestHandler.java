package network;

public class RequestHandler {

    public void handleRequest(String request) {
        Request req = RequestParser.requestParser(request);

        switch (req.getOperation()) {
            case PUT -> {

            }
        }

    }
}
