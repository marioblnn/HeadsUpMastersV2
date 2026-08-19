package auth

import (
	"GoServer/user"
	"encoding/json"
	"log"
	"net/http"
	"time"
)

func ValidateGuest(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("auth_token")
	if err != nil {
		AssignGuest(w, r)
		return
	}
	err = ValidateJWT(cookie.Value)
	if err != nil {
		AssignGuest(w, r)
		return
	}
}

func AssignGuest(w http.ResponseWriter, r *http.Request) {
	g := user.NewGuest()
	token, err := GenerateJWT(g.DisplayName)
	if err != nil {
		http.Error(w, "Could not generate the token", http.StatusInternalServerError)
		return
	}
	cookie := http.Cookie{
		Name:     "auth_token",
		Value:    token,
		Path:     "/",
		Expires:  time.Now().Add(24 * time.Hour),
		MaxAge:   24 * 60 * 60,
		HttpOnly: true,
		Secure:   r.TLS != nil,
		SameSite: http.SameSiteStrictMode,
	}
	http.SetCookie(w, &cookie)
	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(g); err != nil {
		log.Printf("Could not encode the guest: %v", err)
	}

}
