import { ArrowLeft, CircleHelp, Settings2, Volume2 } from "lucide-react";
import { lobbyTables, type LobbyTable } from "../../data/lobby";

function getTableFromPathname(pathname: string): LobbyTable | undefined {
  const match = pathname.match(/^\/table-(\d+)$/);
  if (!match) return undefined;

  return lobbyTables.find((table) => table.id === Number(match[1]));
}

export function TablePage() {
  const table = getTableFromPathname(window.location.pathname);

  if (!table) {
    return null;
  }

  return (
    <main className="table-page">
      <header className="table-page__header">
        <button className="table-page__back" type="button" onClick={() => window.close()}>
          <ArrowLeft size={16} aria-hidden="true" />
          Lobby
        </button>
        <div className="table-page__title">
          <span className="table-page__live-dot" aria-hidden="true" />
          <div>
            <strong>{table.name}</strong>
            <span>{table.stakes} · Heads-up cash game</span>
          </div>
        </div>
        <div className="table-page__tools">
          <button type="button" aria-label="Toggle sound">
            <Volume2 size={17} aria-hidden="true" />
          </button>
          <button type="button" aria-label="Get help">
            <CircleHelp size={17} aria-hidden="true" />
          </button>
          <button type="button" aria-label="Table settings">
            <Settings2 size={17} aria-hidden="true" />
          </button>
        </div>
      </header>

      <section className="table-page__workspace" aria-label={`${table.name} poker table`}>
        <div className="table-page__table-area">
          <div className="table-felt">
            <div className="table-felt__rail" />
          </div>
        </div>
      </section>
    </main>
  );
}
