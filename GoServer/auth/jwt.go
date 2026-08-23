package auth

import (
	"GoServer/config"
	"fmt"
	"time"
	"github.com/golang-jwt/jwt/v5"
	
)


var secretKey = []byte(config.LoadConfig().SecretJWTKey)


func GenerateJWT(uuid string) (string, error) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, 
		jwt.MapClaims{
		"uuid": uuid,
		"exp": time.Now().Add(time.Hour * 2).Unix(),
	})
	tokenString, err := token.SignedString(secretKey)
	if err != nil {
		return "", err
	}
	return tokenString, nil
}


func ExtractIDFromJWT(tokenString string) (string, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
		return secretKey, nil
	})
	if err != nil {
		return "", fmt.Errorf("could not parse the token")
	}
	if !token.Valid {
		return "", fmt.Errorf("invalid token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return "", fmt.Errorf("invalid token claims format")
	}
	uuid, ok := claims["uuid"].(string)
	if !ok {
		return "", fmt.Errorf("username not found in token or username is not a string")
	}
	return uuid, nil
}


