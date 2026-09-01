import type { ButtonHTMLAttributes, CSSProperties } from "react";
import "./PlayingCard.css";
import { getCardLabel, SUIT_METADATA, type CardData } from "./cardConfig";

export interface PlayingCardProps
  extends Omit<ButtonHTMLAttributes<HTMLButtonElement>, "children"> {
  card: CardData;
  faceUp?: boolean;
  selected?: boolean;
  size?: "small" | "medium" | "large";
}

export function PlayingCard({
  card,
  faceUp = true,
  selected = false,
  size = "medium",
  className = "",
  disabled = false,
  style,
  ...buttonProps
}: PlayingCardProps) {
  const suit = SUIT_METADATA[card.suit];
  const label = faceUp ? getCardLabel(card) : "Face-down playing card";
  const classes = [
    "playing-card",
    `playing-card--${size}`,
    faceUp ? "playing-card--face-up" : "playing-card--face-down",
    selected ? "playing-card--selected" : "",
    disabled ? "playing-card--disabled" : "",
    className,
  ]
    .filter(Boolean)
    .join(" ");

  const cardStyle = {
    ...style,
    ["--card-suit" as string]: suit.colorToken,
  } as CSSProperties;

  return (
    <button
      {...buttonProps}
      type={buttonProps.type ?? "button"}
      className={classes}
      style={cardStyle}
      aria-label={label}
      aria-pressed={selected}
      disabled={disabled}
    >
      {faceUp ? (
        <>
          <span className="playing-card__corner" aria-hidden="true">
            <span>{card.rank}</span>
            <span>{suit.symbol}</span>
          </span>
          <span className="playing-card__rank" aria-hidden="true">
            {card.rank}
          </span>
          <span className="playing-card__suit" aria-hidden="true">
            {suit.symbol}
          </span>
        </>
      ) : (
        <span className="playing-card__back-pattern" aria-hidden="true">
          <span>♠</span>
        </span>
      )}
    </button>
  );
}

const demoCards: CardData[] = [
  { rank: "8", suit: "clubs" },
  { rank: "7", suit: "hearts" },
  { rank: "7", suit: "diamonds" },
  { rank: "A", suit: "spades" },
  { rank: "A", suit: "diamonds" },
  { rank: "2", suit: "clubs" },
];

export function PlayingCardPreview() {
  return (
    <section className="card-preview" aria-labelledby="card-preview-title">
      <div className="card-preview__intro">
        <p className="card-preview__eyebrow">Component preview</p>
        <h2 id="card-preview-title">A hand built for every move.</h2>
        <p>Reusable cards with clear states, consistent sizing, and suit colors made for the table.</p>
      </div>
      <div className="card-preview__cards">
        {demoCards.map((card, index) => (
          <PlayingCard
            card={card}
            key={`${card.rank}-${card.suit}`}
            size={index === 4 ? "large" : "medium"}
            selected={index === 1}
            faceUp={index !== 5}
            disabled={index === 4}
          />
        ))}
      </div>
    </section>
  );
}
