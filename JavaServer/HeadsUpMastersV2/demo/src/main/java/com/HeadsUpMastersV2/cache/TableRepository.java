package com.HeadsUpMastersV2.cache;

public interface TableRepository {
    void savePlayerSession(String uuid, String tableId, int seat);
    void removePlayerSession(String uuid);
    boolean isPlayerActive(String uuid);
}
