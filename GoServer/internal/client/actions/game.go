package actions


import (
	tablepb "GoServer/proto/table/v1"
)



type GameEngine struct {
	TableClient tablepb.TableServiceClient
}