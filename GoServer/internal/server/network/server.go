package network

import (
	"fmt"
	"log"
	"net/http"
	"GoServer/auth"
)

func enableCORS(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "http://localhost:5173")
		w.Header().Set("Access-Control-Allow-Credentials", "true")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == http.MethodOptions {
			w.WriteHeader(http.StatusOK)
			return
		}
		next(w, r)
	}
}

func handleHealth(w http.ResponseWriter, r *http.Request){
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}



func NewServer(port string) {
	http.HandleFunc("/health", enableCORS(handleHealth))
	http.HandleFunc("/assign-guest", enableCORS(auth.ValidateGuest))


	fmt.Println("Starting the server...")
	err := http.ListenAndServe(port, nil)
	fmt.Println("Go server is up")

	if err != nil {
		log.Fatal("Server crashed: ", err)
	}
}
