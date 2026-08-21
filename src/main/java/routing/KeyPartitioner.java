package routing;

public class KeyPartitioner {
    private final int numberOfShards;

    public KeyPartitioner(int numberOfShards) {
        this.numberOfShards = numberOfShards;
    }
    public int getShard(String key) {
        int shardNo =  Math.floorMod(key.hashCode(), numberOfShards);
        System.out.println("hash is "+key.hashCode());
        return shardNo;
    }
}
