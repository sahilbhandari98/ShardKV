package node;

import KVStore.WAL.FileWriteAheadLog;
import KVStore.strategy.PersistedKVStore;
import network.ClusterConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class NodeManager {
    private final ClusterConfiguration clusterConfiguration;
    private final KVNode kvNode;

    public NodeManager(ClusterConfiguration clusterConfiguration, String walName) throws IOException {
        this.clusterConfiguration = clusterConfiguration;
        Optional<NodeInfo> nodeInfo = clusterConfiguration
                .getAllNodes()
                .stream()
                .filter(node -> node.getNodeId().equals(clusterConfiguration.getCurrentNodeId()))
                .findAny();
        this.kvNode = new KVNode(clusterConfiguration.getCurrentNodeId(), nodeInfo.get().getPort(),
                new PersistedKVStore<>(new FileWriteAheadLog(Path.of("data",walName))));
    }

    public KVNode getKvNode() {
        return this.kvNode;
    }

    public int clusterSize() {
        return this.clusterConfiguration.getAllNodes().size();
    }

}
