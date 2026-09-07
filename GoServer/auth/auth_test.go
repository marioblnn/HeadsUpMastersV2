package auth

import (
	"testing"

	"github.com/golang-jwt/jwt/v5"
)

func TestGenerateJWT(t *testing.T) {
	t.Parallel()

	const testUUID = "test-uuid-1234"

	token, err := GenerateJWT(testUUID)
	if err != nil {
		t.Fatalf("GenerateJWT() returned an error: %v", err)
	}

	if token == "" {
		t.Fatal("GenerateJWT() returned an empty token")
	}

	gotUUID, err := ExtractIDFromJWT(token)
	if err != nil {
		t.Fatalf("ExtractIDFromJWT() returned an error for a generated token: %v", err)
	}
	if gotUUID != testUUID {
		t.Errorf("ExtractIDFromJWT() = %q, want %q", gotUUID, testUUID)
	}
}

func TestExtractIDFromJWTRejectsInvalidTokens(t *testing.T) {
	t.Parallel()

	tests := []struct {
		name  string
		token string
	}{
		{
			name:  "malformed token",
			token: "not-a-jwt",
		},
		{
			name: "token signed with a different key",
			token: func() string {
				token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{"uuid": "test-uuid-1234"})
				signed, err := token.SignedString([]byte("wrong-key"))
				if err != nil {
					t.Fatalf("could not create invalid test token: %v", err)
				}
				return signed
			}(),
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			t.Parallel()

			gotUUID, err := ExtractIDFromJWT(tt.token)
			if err == nil {
				t.Fatal("ExtractIDFromJWT() returned nil error for an invalid token")
			}
			if gotUUID != "" {
				t.Errorf("ExtractIDFromJWT() returned UUID %q for an invalid token, want empty", gotUUID)
			}
		})
	}
}
