package api

import (
	"GoServer/auth"
	"encoding/json"
	"log/slog"
	"net/http"
)

func (g *APIGateway) HandleGuest(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	createNewGuest := func() {
		guest, cookie, err := auth.GetNewUser(w, r)
		if err != nil {
			slog.Error("failed to create guest user", "error", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		
		http.SetCookie(w, cookie)
		
		dto := guest.ToGuestDTO()
		if err := json.NewEncoder(w).Encode(dto); err != nil {
			http.Error(w, "Could not encode the data", http.StatusInternalServerError)
			return
		}
		
		g.AppCache.SaveUserSession(guest.Uuid, dto)
	}

	cookie, err := r.Cookie("auth_token")
	if err != nil {
		createNewGuest()
		return
	}

	uuid, err := auth.ExtractIDFromJWT(cookie.Value)
	if err != nil {
		createNewGuest()
		return
	}

	user, err := g.AppCache.GetUserSession(uuid)
	if err != nil {
		createNewGuest()
		return
	}

	if err = json.NewEncoder(w).Encode(user); err != nil {
		http.Error(w, "Could not encode user", http.StatusInternalServerError)
	}
}