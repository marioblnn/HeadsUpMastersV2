import { Check, WalletCards, X } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import type { LobbyTable } from "../../data/lobby";

function parseAmount(value: string) {
  return Number(value.replace(/[$,]/g, ""));
}

type JoinTablePageProps = {
  table: LobbyTable;
  onClose: () => void;
};

export function JoinTablePage({ table, onClose }: JoinTablePageProps) {
  const [amount, setAmount] = useState(parseAmount(table.minBuyIn));
  const [joined, setJoined] = useState(false);

  const limits = useMemo(() => ({ min: parseAmount(table.minBuyIn), max: parseAmount(table.maxBuyIn) }), [table]);

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        onClose();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [onClose]);

  const isValidAmount = amount >= limits.min && amount <= limits.max;

  return (
    <div className="join-page" role="presentation" onClick={onClose}>
      <section className="join-card" role="dialog" aria-modal="true" aria-labelledby="join-title" onClick={(event) => event.stopPropagation()}>
        <button className="join-card__close" type="button" onClick={onClose} aria-label="Close buy-in dialog">
          <X size={18} aria-hidden="true" />
        </button>
        <div className="join-card__eyebrow">
          <WalletCards size={15} aria-hidden="true" />
          Join table
        </div>
        <h1 id="join-title">Choose your buy-in</h1>
        <p className="join-card__intro">
          Bring your preferred stack to <strong>{table.name}</strong>.
        </p>

        <div className="join-card__table-summary">
          <span>{table.stakes}</span>
          <span aria-hidden="true">·</span>
          <span>{table.players} players</span>
          <span aria-hidden="true">·</span>
          <span>Heads-up cash game</span>
        </div>

        <label className="buy-in-field" htmlFor="buy-in-amount">
          <span>Buy-in amount</span>
          <div className="buy-in-field__input">
            <span>$</span>
            <input
              id="buy-in-amount"
              type="number"
              min={limits.min}
              max={limits.max}
              step="1"
              value={amount}
              onChange={(event) => setAmount(Number(event.target.value))}
              aria-describedby="buy-in-limits"
            />
          </div>
        </label>
        <p className="buy-in-field__limits" id="buy-in-limits">
          Choose between {table.minBuyIn} and {table.maxBuyIn}
        </p>

        <div className="buy-in-presets" aria-label="Buy-in presets">
          {[limits.min, Math.round((limits.min + limits.max) / 2), limits.max].map((preset) => (
            <button
              key={preset}
              className={amount === preset ? "buy-in-preset buy-in-preset--active" : "buy-in-preset"}
              type="button"
              onClick={() => setAmount(preset)}
            >
              ${preset.toLocaleString()}
            </button>
          ))}
        </div>

        <button
          className="join-card__confirm"
          type="button"
          disabled={!isValidAmount || joined}
          onClick={() => setJoined(true)}
        >
          {joined ? (
            <>
              <Check size={17} aria-hidden="true" />
              Seat reserved
            </>
          ) : (
            "Join table"
          )}
        </button>

        {joined && (
          <p className="join-card__success" role="status">
            Your ${amount.toLocaleString()} stack is ready. Taking you to the table soon.
          </p>
        )}
      </section>
    </div>
  );
}
