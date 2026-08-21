package routing;

public class KeyPartitioner {
    private final int numberOfShards;

    public KeyPartitioner(int numberOfShards) {
        this.numberOfShards = numberOfShards;
    }
    public int getShard(String key) {
        return Math.floorMod(key.hashCode(), numberOfShards);
    }
}
