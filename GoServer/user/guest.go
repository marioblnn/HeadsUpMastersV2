package user

import (
	//"fmt"
	"math/rand"
	"strings"
	"github.com/google/uuid"
)

type Guest struct {
	Uuid  string  `json:"uuid"`
	DisplayName string  `json:"displayName"`
	Balance     float64 `json:"balance"`
}

func NewGuest() Guest {
	username := generateDisplayName()

	return Guest{
		DisplayName: username,
		Balance:     1000.0,
		Uuid:  uuid.NewString(),
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

func (g *Guest) GetUuid() string {
	return g.Uuid
}
