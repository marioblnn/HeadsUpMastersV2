package cache

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
)


type ActiveUser struct{
	DisplayName string `json:"displayName"`
	Balance int64 `json:"balance"`
	ActiveTables []string `json:"activeTables"`
}


func SaveUserSession(r *redis.Client, displayName string, balance int64, tables []string){
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	user := &ActiveUser{
		DisplayName: displayName,
		Balance: balance,
		ActiveTables: tables,
	}
	jsonActiveUser, err := json.Marshal(user)
	if err != nil {
		return 
	}
	r.HSet(ctx, "active-users", jsonActiveUser)
}

func (r* AppRedis) GetUserSession(uuid string) (*ActiveUser, error) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second *3)
	defer cancel()

	userData, err := r.AppCache.HGet(ctx, "active-users", uuid).Result()
	if err != nil {
		return nil, fmt.Errorf("User not found!\n")

	}
	var user ActiveUser;
	err = json.Unmarshal([]byte(userData), &user)
	if err != nil {
		return nil, fmt.Errorf("Could not parse the user: \n")
	}
	return &user, nil
}