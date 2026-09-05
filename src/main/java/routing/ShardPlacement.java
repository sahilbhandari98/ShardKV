package routing;

import node.NodeInfo;

import java.util.List;

public class ShardPlacement {
    NodeInfo primary;
    List<NodeInfo> replicas;

    public ShardPlacement(NodeInfo primary, List<NodeInfo> replicas) {
        this.primary = primary;
        this.replicas = replicas;
    }

    public NodeInfo getPrimary() {
        return primary;
    }

    public List<NodeInfo> getReplicas() {
        return replicas;
    }
}
