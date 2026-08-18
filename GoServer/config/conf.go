package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Conf struct {
	FrontendURL   string
	JavaServerURL string
	GoGatewayURl  string
}

func LoadConfig() *Conf {
	err := godotenv.Load()
	if err != nil {
		log.Printf("Could not load config files! Relying on sys variables! err: %v \n", err)
	}


	return &Conf{
		FrontendURL:   os.Getenv("FRONTEND_URL"),
		JavaServerURL: os.Getenv("JAVA_SERVER_URL"),
		GoGatewayURl:  os.Getenv("FRONTEND_URL"),
	}
}
