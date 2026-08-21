package routing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class KeyPartitionerTest {

    @Test
    public void shouldReturnSameShardForSameKey() {
        KeyPartitioner keyPartitioner = new KeyPartitioner(3);
        int shard = keyPartitioner.getShard("user:1");
        int shard1 = keyPartitioner.getShard("user:1");

        assertEquals(shard, shard1);
    }

    @Test
    public void shouldReturnDifferentShardForDifferentKey() {
        KeyPartitioner keyPartitioner = new KeyPartitioner(3);
        int shard = keyPartitioner.getShard("user:1");
        int shard1 = keyPartitioner.getShard("user:2");

        assertNotEquals(shard, shard1);
    }
}
