package actions

import (
	
	tablepb "GoServer/proto/table/v1"
	"context"
	"fmt"
	"log"
	"GoServer/internal/client/network"
)

var beConn, err = network.EstablishConn()

func JoinTable(tableId string, guest string, seat int32, amount int64){
	tableClient := tablepb.NewTableServiceClient(beConn)
	response, err := tableClient.JoinTable(context.Background(), &tablepb.JoinTableRequest{
		TableId: tableId,
		Guest: guest,
		Seat: seat,
		BuyInAmount: amount,
	})
	if err != nil {
		log.Printf("ERROR, %v", err)
		return
	}
	fmt.Printf("%s tried to join %s table at seat %d |", guest, tableId, seat)
	fmt.Printf("status--> %s, error: %s \n", response.Message, response.Error)
	defer beConn.Close()
}