package KVStore.WAL;

import java.util.List;

public interface WriteAheadLog {
    void append(WalRecord walRecord);
    List<WalRecord> readAll();
}
