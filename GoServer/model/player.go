package model


type Player struct{
	DisplayName string  `json:"displayName"`
	Stack     float64 `json:"balance"`
}


func newPlayer(displayName string, stack float64) Player {
	return Player {
		DisplayName: displayName,
		Stack: stack,
		
	}
}