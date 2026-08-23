package actions

import (
	tablepb "GoServer/proto/table/v1"
	"context"
	"fmt"
	"log"
	"strings"
	"time"

	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	//"GoServer/internal/client/network"
)

func ViewTables(r *redis.Client) (string, error) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*2)
	defer cancel()
	val, err := r.HVals(ctx, "view-tables").Result()
	if err != nil {
		return "", fmt.Errorf("Could not load the table view: v% \n", err);
	}
	
	return "[" + strings.Join(val, ",") + "]", nil
}

func (hconn *GameEngine) JoinTable (tableId string, uuid string, seat int32, amount int64) (bool, string) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()

	tableClient := hconn.TableClient
	response, err := tableClient.JoinTable(ctx, &tablepb.JoinTableRequest{
		TableId:     tableId,
		Uuid:        uuid,
		Seat:        seat,
		BuyInAmount: amount,
	})
	if err != nil {
		return false, fmt.Sprintf("Could not parse the tables in the server-end: %v", err )
	}
	fmt.Printf("%s tried to join %s, seat %d |", uuid, tableId, seat)
	fmt.Printf("status--> %s, error: %s \n", response.Message, response.Error)
	return response.Success, fmt.Sprintln(response.Error)
}


func LeaveTable(conn *grpc.ClientConn, tableId string, uuid string, seat int32) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()
	tableClient := tablepb.NewTableServiceClient(conn)
	response, err := tableClient.LeaveTable(ctx, &tablepb.LeaveTableRequest{
		TableId: tableId,
		Uuid:    uuid,
		Seat:    seat,
	})
	if err != nil {
		log.Printf("ERROR: %v", err)
		return
	}
	fmt.Printf("%s tried to leave %s, seat %d|", uuid, tableId, seat)
	fmt.Printf("status--> %t, error: %s \n", response.Success, response.Error)
}
