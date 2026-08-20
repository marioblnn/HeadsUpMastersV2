package actions

import (
	tablepb "GoServer/proto/table/v1"
	"context"
	"fmt"
	"log"
	"time"

	"google.golang.org/grpc"
	//"GoServer/internal/client/network"
)


func JoinTable(conn *grpc.ClientConn, tableId string, uuid string, seat int32, amount int64){
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*2)
	defer cancel()
	tableClient := tablepb.NewTableServiceClient(conn)
	response, err := tableClient.JoinTable(ctx, &tablepb.JoinTableRequest{
		TableId: tableId,
		Uuid: uuid,
		Seat: seat,
		BuyInAmount: amount,
	})
	if err != nil {
		log.Printf("ERROR, %v", err)
		return
	}
	fmt.Printf("%s tried to join %s, seat %d |", uuid, tableId, seat)
	fmt.Printf("status--> %s, error: %s \n", response.Message, response.Error)
}


func LeaveTable(conn *grpc.ClientConn, tableId string, uuid string, seat int32){
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*2)
	defer cancel()
	tableClient := tablepb.NewTableServiceClient(conn)
	response, err := tableClient.LeaveTable(ctx, &tablepb.LeaveTableRequest{
		TableId: tableId,
		Uuid: uuid,
		Seat: seat,
	});
	if err != nil {
		log.Printf("ERROR: %v", err)
		return
	}
	fmt.Printf("%s tried to leave %s, seat %d|", uuid, tableId, seat)
	fmt.Printf("status--> %t, error: %s \n", response.Success, response.Error)	
}