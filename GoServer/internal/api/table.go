package api

import (
	"GoServer/auth"
	"encoding/json"
	"fmt"
	"net/http"

)

type JoinRequestModel struct{
	TableId string `json:"tableId"`
	Seat int32 `json:"seat"`
	BuyIn int64 `json:"buyIn"`
}

type JoinResponseModel struct{
	Success bool `json:"success"`
	Message string `json:"message"`
}




func (hconn *APIGateway)JoinTableRequest(w http.ResponseWriter, r * http.Request){

	token, err := r.Cookie("auth_token")
	if err != nil {
		http.Error(w, "Auth token not found", http.StatusUnauthorized)
		return
	}

	uuid, err := auth.ExtractIDFromJWT(token.Value)
	if err != nil {
		fmt.Println(err)
		return
	}

	var req JoinRequestModel
	err = json.NewDecoder(r.Body).Decode(&req)
	if err != nil {
		http.Error(w, "Request is malformed", http.StatusBadRequest)
		return
	}

	success, msg := hconn.Engine.JoinTable(req.TableId, uuid, req.Seat, req.BuyIn)
	resp := json.NewEncoder(w).Encode(JoinResponseModel{
		Success: success,
		Message: msg,
	})

	err = json.NewEncoder(w).Encode(resp)
	if err != nil {
		fmt.Printf("Failed to encode response: %v\n", err)
	}

}