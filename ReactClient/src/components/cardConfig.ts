export type CardRank =
  | "A"
  | "2"
  | "3"
  | "4"
  | "5"
  | "6"
  | "7"
  | "8"
  | "9"
  | "10"
  | "J"
  | "Q"
  | "K";

export type CardSuit = "clubs" | "hearts" | "diamonds" | "spades";

export interface CardData {
  rank: CardRank;
  suit: CardSuit;
}

interface SuitMetadata {
  symbol: string;
  name: string;
  colorToken: string;
}

export const SUIT_METADATA: Record<CardSuit, SuitMetadata> = {
  clubs: { symbol: "♣", name: "Clubs", colorToken: "var(--card-clubs)" },
  hearts: { symbol: "♥", name: "Hearts", colorToken: "var(--card-hearts)" },
  diamonds: { symbol: "♦", name: "Diamonds", colorToken: "var(--card-diamonds)" },
  spades: { symbol: "♠", name: "Spades", colorToken: "var(--card-spades)" },
};

const rankNames: Partial<Record<CardRank, string>> = {
  A: "Ace",
  J: "Jack",
  Q: "Queen",
  K: "King",
};

export function getCardLabel(card: CardData): string {
  return `${rankNames[card.rank] ?? card.rank} of ${SUIT_METADATA[card.suit].name}`;
}
