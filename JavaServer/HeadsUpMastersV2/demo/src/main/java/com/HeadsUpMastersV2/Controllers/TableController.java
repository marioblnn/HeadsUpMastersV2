package com.HeadsUpMastersV2.Controllers;

import io.grpc.stub.StreamObserver;
import com.HeadsUpMastersV2.proto.table.v1.TableServiceGrpc;
import com.HeadsUpMastersV2.proto.table.v1.JoinTableRequest;
import com.HeadsUpMastersV2.proto.table.v1.JoinTableResponse;
import com.HeadsUpMastersV2.GameBuilder.GuestPlayer;
import com.HeadsUpMastersV2.GameBuilder.TableUpdate;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableController extends TableServiceGrpc.TableServiceImplBase {

    TableUpdate Table1 = new TableUpdate("table-001", 1, 2, 100, 200);
    TableUpdate Table2 = new TableUpdate("table-002", 1, 2, 100, 200);
    TableUpdate Table3 = new TableUpdate("table-003", 1, 2, 100, 200);
    TableUpdate Table4 = new TableUpdate("table-004", 1, 2, 100, 200);

    Map<String, TableUpdate> TablesMap = new ConcurrentHashMap<>(Map.of(
            "table-001", Table1,
            "table-002", Table2,
            "table-003", Table3,
            "table-004", Table4
        ));



    public void joinTableResponseTemplate(boolean success, String message, String error, StreamObserver<JoinTableResponse> responseObserver){
        JoinTableResponse response = JoinTableResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .setError(error)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void joinTable(JoinTableRequest request, StreamObserver<JoinTableResponse> responseObserver){
        try{
            TableUpdate table = TablesMap.get(request.getTableId());
            if (table == null){
                System.out.println(request.getGuest() + " tried to join table: " + request.getTableId() + " but failed: Table not found" + "\n");
                joinTableResponseTemplate(false, "Could not join the table", "Table not found", responseObserver);
                return;
            }
            synchronized (table){
                if(!table.checkSeatAvailability(request.getSeat())){
                    System.err.println(request.getGuest() + " tried to join table: " + request.getTableId() + " but failed: Seat not open" + "\n");
                    joinTableResponseTemplate(false, "Could not join the table", "Seat not open: " + request.getSeat(), responseObserver);
                    return;
                }
                if(request.getBuyInAmount() < table.getMinbuyIn()){
                    System.err.println(request.getGuest() + " tried to join table: " + request.getTableId() + " but failed: Not enough funds" + "\n");
                    joinTableResponseTemplate(false, "Could not join the table", "Not enough funds:" + request.getBuyInAmount(), responseObserver);
                    return;
                } 
            String guestId = request.getGuest();
            table.seatPlayer(new GuestPlayer(guestId, request.getBuyInAmount(), request.getSeat()));
            System.out.print(request.getGuest() + " has joined the table: " + request.getTableId() + " at seat " + request.getSeat() + "\n");
            joinTableResponseTemplate(true, "Successfully joined table "+ table.getTableID(), "null", responseObserver);
            }
        } catch (Exception e) {
            System.err.println("Error has occured: " + e.getMessage());
        } 
    }
}