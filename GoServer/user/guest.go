package user

import (
	"fmt"
	"math/rand"
	"strings"
	"GoServer/auth"
)

type Guest struct {
	identifier  string
	displayName string
	balance     float64
}

func NewGuest() Guest {
	username := generateDisplayName()
	token, err := auth.GenerateJWT(username)
	if err != nil {
		fmt.Println("Error generating JWT:", err)
		token = ""
	}
	return Guest{
		displayName: username,
		balance:     1000.0,
		identifier:  token,
	}
}

func generateDisplayName() string {
	chars := "0123456789"
	var identifier strings.Builder
	identifier.WriteString("Guest-")
	for range 4{
		identifier.WriteByte(chars[rand.Intn(len(chars))])
	}
	return identifier.String()
}

func (g *Guest) GetDisplayName() string {
	return g.displayName
}

func (g *Guest) GetBalance() float64 {
	return g.balance
}

func (g *Guest) GetIdentifier() string {
	return g.identifier
}