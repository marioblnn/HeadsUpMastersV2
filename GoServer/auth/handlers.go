package auth

import (
	"GoServer/model"
	"fmt"
	"net/http"
	"time"
)



func CreateUser(_ http.ResponseWriter, r *http.Request,) (*model.Guest, *http.Cookie, error){
	g := model.NewGuest()
	token, err := GenerateJWT(g.Uuid)
	if err != nil {
		return nil, nil, fmt.Errorf("Could not generate the token")
	}
	cookie := http.Cookie{
		Name:     "auth_token",
		Value:    token,
		Path:     "/",
		Expires:  time.Now().Add(24 * time.Hour),
		MaxAge:   24 * 60 * 60,
		HttpOnly: true,
		Secure:   r.TLS != nil,
		SameSite: http.SameSiteLaxMode,
	}
	return &g, &cookie, nil

}
