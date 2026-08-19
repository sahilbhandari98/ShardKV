package KVStore.WAL;

import java.io.IOException;
import java.util.List;

public class FailingWriteAheadLog implements WriteAheadLog{
    @Override
    public void append(WalRecord walRecord) throws IOException {
        throw new IOException("Simulated disk failure");
    }

    @Override
    public List<WalRecord> readAll() throws IOException {
        return List.of();
    }
}
