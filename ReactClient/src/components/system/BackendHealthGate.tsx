import { PlayingCard } from "../poker/PlayingCard";
import type { CardData } from "../poker/cardConfig";
import { useBackendHealthGate } from "../../hooks/useBackendHealthGate";

function ServerUnreachableScreen() {
  const cards: CardData[] = [
    { rank: "A", suit: "hearts" },
    { rank: "K", suit: "hearts" },
    { rank: "Q", suit: "hearts" },
    { rank: "J", suit: "hearts" },
    { rank: "10", suit: "hearts" },
  ];

  return (
    <div className="server-blocker" role="status" aria-live="polite" aria-label="Backend unavailable">
      <div className="server-blocker__card">
        <div className="server-blocker__hand" aria-hidden="true">
          {cards.map((card, index) => (
            <PlayingCard
              key={`${card.rank}-${card.suit}`}
              card={card}
              faceUp
              size="small"
              disabled
              className="server-blocker__hand-card"
              style={{ ["--card-delay" as string]: `${index * 0.22}s` }}
            />
          ))}
        </div>
        <div className="server-blocker__content">
          <span className="server-blocker__label">Server unreachable...</span>
          <span className="server-blocker__hint">Retrying connection...</span>
        </div>
      </div>
    </div>
  );
}

export function BackendHealthGate({ children }: { children: React.ReactNode }) {
  const isBackendReady = useBackendHealthGate();

  if (!isBackendReady) {
    return (
      <>
        {children}
        <ServerUnreachableScreen />
      </>
    );
  }

  return <>{children}</>;
}
