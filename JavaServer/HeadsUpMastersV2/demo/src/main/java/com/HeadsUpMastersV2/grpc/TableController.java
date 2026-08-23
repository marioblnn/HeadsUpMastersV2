package com.HeadsUpMastersV2.grpc;

import io.grpc.stub.StreamObserver;
import com.HeadsUpMastersV2.proto.table.v1.TableServiceGrpc;
import com.HeadsUpMastersV2.game.GuestPlayer;
import com.HeadsUpMastersV2.game.TableUpdate;
import com.HeadsUpMastersV2.proto.table.v1.JoinTableRequest;
import com.HeadsUpMastersV2.proto.table.v1.JoinTableResponse;
import com.HeadsUpMastersV2.proto.table.v1.LeaveTableRequest;
import com.HeadsUpMastersV2.proto.table.v1.LeaveTableResponse;
import com.HeadsUpMastersV2.cache.SessionRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TableController extends TableServiceGrpc.TableServiceImplBase {
    SessionRepository sr = new SessionRepository();

    TableUpdate Table1 = new TableUpdate("table-001", 1, 2, 100, 200);
    TableUpdate Table2 = new TableUpdate("table-002", 1, 2, 100, 200);
    TableUpdate Table3 = new TableUpdate("table-003", 1, 2, 100, 200);
    TableUpdate Table4 = new TableUpdate("table-004", 1, 2, 100, 200);

    Map<String, TableUpdate> TablesMap = new ConcurrentHashMap<>(Map.of(
            "table-001", Table1,
            "table-002", Table2,
            "table-003", Table3,
            "table-004", Table4));

    private void joinTableResponseTemplate(boolean success, String message, String error,
            StreamObserver<JoinTableResponse> responseObserver) {
        JoinTableResponse response = JoinTableResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .setError(error)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void leaveTableResponseTemplate(boolean success, String error,
            StreamObserver<LeaveTableResponse> responseObserver) {
        LeaveTableResponse response = LeaveTableResponse.newBuilder()
                .setSuccess(success)
                .setError(error)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void testPublish(){
        
    }


    @Override
    public void joinTable(JoinTableRequest request, StreamObserver<JoinTableResponse> responseObserver) {
        try {
            TableUpdate table = TablesMap.get(request.getTableId());
            if (table == null) {
                System.out.println("! Join table: " + request.getTableId() + " not found" + "\n");
                joinTableResponseTemplate(false, "Could not join the table", "Table not found", responseObserver);
                return;
            }
            synchronized (table) {
                if (!table.checkSeatAvailability(request.getSeat())) {
                    System.out.println("! Join table: " + table.getTableId() + " seat not open" + "\n");
                    joinTableResponseTemplate(false, "Could not join the table", "Seat not open: " + request.getSeat(),
                            responseObserver);
                    return;
                }
                if (request.getBuyInAmount() < table.getMinbuyIn()) {
                    System.out.println("! Join table: " + table.getTableId() + " not enough funds" + "\n");
                    joinTableResponseTemplate(false, "Could not join the table",
                            "Not enough funds:" + request.getBuyInAmount(), responseObserver);
                    return;
                }
                String guestId = request.getUuid();
                table.seatPlayer(new GuestPlayer(guestId, request.getBuyInAmount(), request.getSeat()));
                System.out.print("Join table: " + table.getTableId() + " - " + request.getUuid() + " has joined seat "
                        + request.getSeat() + " for " + request.getBuyInAmount() + "\n");
                joinTableResponseTemplate(true, "Successfully joined table " + table.getTableId(), "null",
                        responseObserver);
            }
        } catch (Exception e) {
            System.err.println("Error has occured: " + e.getMessage());
        }
    }

    @Override
    public void leaveTable(LeaveTableRequest request, StreamObserver<LeaveTableResponse> responseObserver) {
        try {
            TableUpdate table = TablesMap.get(request.getTableId());
            // TODO: implement redis, read active players and store it in Players map
            if (table == null) {
                System.out.println("! Leave table: " + request.getTableId() + " not found" + "\n");
                leaveTableResponseTemplate(false, "Table not found", responseObserver);
                return;
            }
            synchronized (table) {
                GuestPlayer players[] = table.getPlayers();
                int seat = request.getSeat();
                if (players == null || seat < 0 || seat > 1) {
                    System.out.println("! Leave table: invalid request data");
                    leaveTableResponseTemplate(false, "Inavlid data", responseObserver);
                    return;
                }

                GuestPlayer player = players[seat];
                if (player == null) {
                    System.out.print("! Leave table: " + table.getTableId() + " : Seat empty already" + "\n");
                    leaveTableResponseTemplate(false, "Seat already empty", responseObserver);
                    return;
                }

                if (!request.getUuid().equals(player.getGuestUUID())) {
                    System.out
                            .println("! Leave table: " + table.getTableId() + " : Seat " + seat + " not owned" + "\n");
                    leaveTableResponseTemplate(false, "Seat not owned", responseObserver);
                    return;
                }
                // TODO: Modify once redis is done
                table.removePlayer(table.getPlayers()[request.getSeat()]);
                System.out.print(
                        "Leave table: " + table.getTableId() + " - " + player.getGuestUUID() + " has left" + "\n");
                leaveTableResponseTemplate(true, "null", responseObserver);

            }
        } catch (Exception e) {
            System.err.println("Error has occured: " + e.getMessage());
            e.printStackTrace();
            responseObserver.onError(
                    io.grpc.Status.INTERNAL.withDescription("Internal server error: " + e.getMessage())
                            .asRuntimeException());
        }

    }
}