package com.HeadsUpMastersV2.cache;

import com.HeadsUpMastersV2.game.TableUpdate;
import com.google.gson.Gson;
import redis.clients.jedis.JedisPooled;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import com.HeadsUpMastersV2.dto.LobbyTableModel;

public class SessionRepository {
    private JedisPooled jedis;
    private Gson gson = new Gson();

    public SessionRepository() {
        this.jedis = RedisConfig.getJedis();
    }

    public void saveActivePlayer(String uuid) {
        jedis.set(uuid, "hi");
    }

    public void saveActiveTables(String tableId, TableUpdate tableObject) {
        String tableJson = gson.toJson(tableObject);
        jedis.hset("active-tables", tableId, tableJson);

    }

    public void saveTableLobby(String tableId, LobbyTableModel lobbyTableDTO) {
        String tableJson = gson.toJson(lobbyTableDTO);
        jedis.hset("view-tables", tableId, tableJson);
    }

    public void loadTable(String tableID) {
        System.out.println(jedis.hget("active-tables", tableID));
    }

    public void testPublish(LobbyTableModel table) {
        // Run in a background thread so it doesn't block server startup
        new Thread(() -> {
            while (true) {
                try {
                    // Send an update every 5 seconds
                    Thread.sleep(5000); 
                    
                    
                    if (table.getPlayerCount() == 1 ){
                        table.decreasePlayerCount();
                    }
                    else {
                        table.increasePlayereCount();
                    }
                    
                    String tbJSON = gson.toJson(table);
                    jedis.hset("view-tables", table.getTableId(), tbJSON);
                    jedis.publish("view-tables", tbJSON);
                    
                    System.out.println("Updates sent from Java");
                    System.out.println(jedis.hget("view-tables", table.getTableId()));
                } catch (Exception e) {
                    System.out.println("Publish error: " + e.getMessage());
                }
            }
        }).start();
    }

    public List<LobbyTableModel> getLobbyActiveTables() {
        List<LobbyTableModel> activeTablesDTO = new ArrayList<>();
        List<String> redisTables = jedis.hvals("active-tables");
        for (String redisTable : redisTables) {
            TableUpdate table = gson.fromJson(redisTable, TableUpdate.class);
            activeTablesDTO.add(table.getLobbyTableDTO());
        }
        return activeTablesDTO;
    }

    public void loadPlayers() {
        String player = jedis.get("player");
        System.out.println(player);

    }

}
