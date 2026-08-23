package network;

import node.NodeInfo;

import java.util.List;

public class ClusterConfiguration {
    private String currentNodeId;
    List<NodeInfo> allNodes;

    public ClusterConfiguration(String nodeId, List<NodeInfo> kvNodes) {
        this.currentNodeId = nodeId;
        this.allNodes = kvNodes;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public List<NodeInfo> getAllNodes() {
        return allNodes;
    }
}
