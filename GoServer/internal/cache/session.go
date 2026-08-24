package cache

import (
	"GoServer/model"
	"context"
	"encoding/json"
	"fmt"
	"time"
)




func (r *AppRedis) SaveUserSession(uuid string, g *model.GuestDTO) error {
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	
	jsonActiveUser, err := json.Marshal(g)
	if err != nil {
		return fmt.Errorf("Could not parse user into json")
	}
	r.AppCache.HSet(ctx, "active-users", uuid, jsonActiveUser)
	return nil
}


func (r* AppRedis) GetUserSession(uuid string) (*model.GuestDTO, error) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second *3)
	defer cancel()

	userData, err := r.AppCache.HGet(ctx, "active-users", uuid).Result()
	if err != nil {
		return nil, fmt.Errorf("User not found!\n")

	}
	var user model.GuestDTO;
	err = json.Unmarshal([]byte(userData), &user)
	if err != nil {
		return nil, fmt.Errorf("Could not parse the user: \n")
	}
	return &user, nil
}