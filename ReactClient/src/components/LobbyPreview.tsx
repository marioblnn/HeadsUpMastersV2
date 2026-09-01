import { useState } from "react";
import { lobbyTables, type LobbyTable } from "../data/lobby";

function LobbyToolbar() {
  return (
    <div className="lobby-preview__toolbar">
      <div className="lobby-preview__leftbar">
        <div className="lobby-logo" aria-label="Heads Up Masters logo">
          <span className="lobby-logo__mark">M</span>
        </div>
        <span className="lobby-username">Mario</span>
      </div>
      <div className="lobby-tabs" role="tablist" aria-label="Poker game categories">
        <span className="lobby-tab lobby-tab--active">Cash Game</span>
      </div>
      <div className="lobby-wallet" aria-label="Wallet information">
        <span className="lobby-wallet__label">Balance</span>
        <strong>$1,000.00</strong>
      </div>
    </div>
  );
}

function LobbyFilters() {
  return (
    <div className="lobby-preview__filters">
      <div className="lobby-game-type">Texas No Limit Hold'em</div>
    </div>
  );
}

function getOccupancyClass(occupancy: LobbyTable["occupancy"]) {
  if (occupancy === 1) return "lobby-row__players-badge--full";
  if (occupancy === 0.5) return "lobby-row__players-badge--half";
  return "lobby-row__players-badge--open";
}

function LobbyTableRow({ table }: { table: LobbyTable }) {
  return (
    <article className="lobby-row" role="listitem" key={table.id}>
      <div className={`lobby-row__thumb lobby-row__thumb--${table.color}`}>
        <span className="lobby-row__thumb-inner" aria-hidden="true" />
      </div>

      <div className="lobby-row__meta">
        <strong>{table.name}</strong>
        <span>Texas No Limit Hold'em</span>
        <small>Dealer: {table.dealer}</small>
      </div>

      <div className="lobby-row__stakes">{table.stakes}</div>
      <div className="lobby-row__buyin">
        <strong>{table.minBuyIn}</strong>
        <span>Min</span>
      </div>
      <div className="lobby-row__buyin">
        <strong>{table.maxBuyIn}</strong>
        <span>Max</span>
      </div>
      <div className="lobby-row__players">
        <span
          className={`lobby-row__players-badge ${getOccupancyClass(table.occupancy)}`}
          style={{ ["--seat-fill" as any]: table.occupancy }}
          aria-label={`Seat occupancy ${table.occupancy * 100}%`}
        >
          <span />
        </span>
        <span>{table.players}</span>
      </div>

      <div className="lobby-row__actions">
        <button className="lobby-row__play" type="button">
          Join
        </button>
      </div>
    </article>
  );
}

export function LobbyPreview() {
  const INITIAL_TABLE_COUNT = 4;
  const [isExpanded, setIsExpanded] = useState(false);
  const shouldShowToggle = lobbyTables.length > INITIAL_TABLE_COUNT;
  const visibleTables = isExpanded ? lobbyTables : lobbyTables.slice(0, INITIAL_TABLE_COUNT);

  return (
    <section className="lobby-preview" aria-labelledby="lobby-title">
      <div className="lobby-preview__panel">
        <LobbyToolbar />
        <LobbyFilters />

        <div className="lobby-list" role="list" aria-label="Available tables">
          <div className="lobby-list__header" aria-hidden="true">
            <span>Tables</span>
            <span>1v1</span>
            <span>Blinds</span>
            <span>Min Buy-In</span>
            <span>Max Buy-In</span>
            <span>Players</span>
          </div>

          {visibleTables.map((table) => (
            <LobbyTableRow key={table.id} table={table} />
          ))}

          {shouldShowToggle && (
            <button
              type="button"
              className="lobby-preview__toggle"
              aria-expanded={isExpanded}
              onClick={() => setIsExpanded((current) => !current)}
            >
              {isExpanded ? "Show less" : "Show more"}
            </button>
          )}
        </div>
      </div>
    </section>
  );
}
