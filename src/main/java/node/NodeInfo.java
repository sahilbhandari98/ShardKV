package node;

public class NodeInfo {
    private String nodeId;
    private int port;

    public NodeInfo(String nodeId, int port) {
        this.nodeId = nodeId;
        this.port = port;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getPort() {
        return port;
    }
}
