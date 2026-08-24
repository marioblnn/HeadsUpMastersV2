package api

import (
	"GoServer/auth"
	"encoding/json"
	"fmt"
	"net/http"
)

func (g *APIGateway) HandleGuest(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("auth_token")
	if err != nil {
		guest, cookie, err := auth.CreateUser(w, r)
		if err != nil {
			fmt.Println(err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		http.SetCookie(w, cookie)
		err = json.NewEncoder(w).Encode(guest.ToGuestDTO())
		return
		//save to redis
	}
	_, err = auth.ExtractIDFromJWT(cookie.Value)
	if err != nil {
		guest, cookie, err := auth.CreateUser(w, r)
		if err != nil {
			fmt.Println(err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		http.SetCookie(w, cookie)
		err = json.NewEncoder(w).Encode(guest.ToGuestDTO())
		return
		//save to redis
	}
	//fetch from redis
}