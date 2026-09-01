import { ArrowRight, Club } from "lucide-react";
import { PlayingCard } from "./PlayingCard";
import type { CardData } from "./cardConfig";

const tablePreviewCards: CardData[] = [
  { rank: "8", suit: "clubs" },
  { rank: "7", suit: "hearts" },
  { rank: "7", suit: "diamonds" },
  { rank: "A", suit: "spades" },
  { rank: "A", suit: "diamonds" },
];

const handPreviewCards: CardData[] = [
  { rank: "8", suit: "spades" },
  { rank: "8", suit: "hearts" },
];

function GridPlaceholder() {
  return (
    <section className="grid-preview" aria-label="Game board preview">
      <div className="grid-preview__heading">
        <span>Table preview</span>
        <span className="grid-preview__live">
          <span aria-hidden="true" />
          Tables open
        </span>
      </div>
      <div className="poker-table">
        <div className="poker-table__felt">
          <div className="table-preview__cards">
            {tablePreviewCards.map((card) => (
              <PlayingCard
                card={card}
                key={`${card.rank}-${card.suit}`}
                size="small"
              />
            ))}
          </div>
        </div>
      </div>
      <div className="player-hand">
        <div className="player-hand__cards">
          {handPreviewCards.map((card) => (
            <PlayingCard
              card={card}
              key={`${card.rank}-${card.suit}`}
              size="medium"
            />
          ))}
        </div>
        <div className="table-actions">
          <button className="table-action table-action--all-in" type="button">
            All in <span>↑</span>
          </button>
          <button className="table-action table-action--fold" type="button">
            Fold
          </button>
        </div>
      </div>
      <p className="grid-preview__caption">Poker's toughest gamemode.</p>
    </section>
  );
}

export function Hero() {
  return (
    <main>
      <section className="hero" aria-labelledby="hero-title">
        <div className="hero__copy">
          <div className="eyebrow">
            <span className="eyebrow__content">
              <Club className="eyebrow__club" size={15} strokeWidth={2.4} aria-hidden="true" />
              <span>
                Crush Heads-Up <em>Solve the game</em>
              </span>
            </span>
          </div>
          <h1 id="hero-title">
            Squeeze, Trap
            <br />
            <span>Take it all.</span>
          </h1>
          <p className="hero__description">
            The ultimate one-on-one poker game. Face off against friends, challenge real players,
            or practice on bots.
          </p>
          <button className="primary-button" type="button">
            Play Now
            <ArrowRight size={17} strokeWidth={2.4} aria-hidden="true" />
          </button>
          <p className="hero__note">Free to play</p>
        </div>
        <GridPlaceholder />
      </section>
    </main>
  );
}
