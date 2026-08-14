package KVStore.WAL;

import java.io.IOException;
import java.util.List;

public interface WriteAheadLog {
    void append(WalRecord walRecord) throws IOException;
    List<WalRecord> readAll() throws IOException;
}
