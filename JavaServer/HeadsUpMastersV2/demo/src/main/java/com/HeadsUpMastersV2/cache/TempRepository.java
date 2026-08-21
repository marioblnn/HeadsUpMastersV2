package com.HeadsUpMastersV2.cache;


import com.HeadsUpMastersV2.game.TableUpdate;
import java.util.ArrayList;
import java.util.List;

public class TempRepository {

    public static void loadTablesIntoRedis() {
        SessionRepository sr = new SessionRepository();

        TableUpdate table1 = new TableUpdate("table-001", 1, 2, 100, 200);
        TableUpdate table2 = new TableUpdate("table-002", 1, 2, 100, 200);
        TableUpdate table3 = new TableUpdate("table-003", 2, 5, 250, 500);
        TableUpdate table4 = new TableUpdate("table-004", 2, 5, 250, 500);
        TableUpdate table5 = new TableUpdate("table-005", 5, 10, 500, 1000);
        TableUpdate table6 = new TableUpdate("table-006", 5, 10, 500, 1000);
        TableUpdate table7 = new TableUpdate("table-007", 10, 20, 1000, 2000);
        TableUpdate table8 = new TableUpdate("table-008", 10, 20, 1000, 2000);
        TableUpdate table9 = new TableUpdate("table-009", 25, 50, 2500, 5000);

        List<TableUpdate> activeTables = new ArrayList<TableUpdate>(List.of(
                table1, table2, table3, table4, table5, table6, table7, table8, table9));
        
        try{
             for (TableUpdate table : activeTables) {
            sr.saveActiveTables(table.getTableId(), table);
            sr.saveTableLobby(table.getTableId(), table.getLobbyTableDTO());
        }
        System.out.println("Cached raw & preview tables into redis");
        } catch (Exception e){
            System.err.println("Could not cache tables: " + e.getMessage());
        }
       
        
    }



}
