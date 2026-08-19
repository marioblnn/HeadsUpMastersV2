package user

import (
	//"fmt"
	"math/rand"
	"strings"
)

type Guest struct {
	Identifier  string  `json:"identifier"`
	DisplayName string  `json:"displayName"`
	Balance     float64 `json:"balance"`
}

func NewGuest() Guest {
	username := generateDisplayName()

	return Guest{
		DisplayName: username,
		Balance:     1000.0,
		Identifier:  "leave it like this",
	}
}

func generateDisplayName() string {
	chars := "0123456789"
	var identifier strings.Builder
	identifier.WriteString("Guest-")
	for range 4 {
		identifier.WriteByte(chars[rand.Intn(len(chars))])
	}
	return identifier.String()
}

func (g *Guest) GetDisplayName() string {
	return g.DisplayName
}

func (g *Guest) GetBalance() float64 {
	return g.Balance
}

func (g *Guest) GetIdentifier() string {
	return g.Identifier
}
