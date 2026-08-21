package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Conf struct {
	FrontendURL    string
	JavaServerURL  string
	GoGatewayURL   string
	RedisServerURL string
	SecretJWTKey   string
}

func LoadConfig() *Conf {
	err := godotenv.Load()
	if err != nil {
		log.Printf("Could not load config files! Relying on sys variables! err: %v \n", err)
	}

	return &Conf{
		FrontendURL:    os.Getenv("FRONTEND_URL"),
		JavaServerURL:  os.Getenv("JAVA_SERVER_URL"),
		GoGatewayURL:   os.Getenv("FRONTEND_URL"),
		RedisServerURL: os.Getenv("REDIS_SERVER_URL"),
		SecretJWTKey:   os.Getenv("SECRET_JWT_KEY"),
	}
}
