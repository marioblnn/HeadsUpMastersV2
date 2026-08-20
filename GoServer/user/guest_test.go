package user

import (
	"fmt"
	"testing"
)

func TestNewGuest(t *testing.T){
	g := NewGuest()
	fmt.Println(g.GetIdentifier())
}