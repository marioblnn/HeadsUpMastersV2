import { BackendHealthGate } from "../components/system/BackendHealthGate";
import { Header } from "../components/layout/PokerHeader";
import { Hero } from "../components/poker/PokerHero";
import { LobbyPreview } from "../components/lobby/LobbyPreview";
import { TablePage } from "../components/table/TablePage";

function App() {
  if (/^\/table-[^/]+$/.test(window.location.pathname)) {
    return <TablePage />;
  }

  return (
    <BackendHealthGate>
      <div className="app-shell">
        <Header />
        <Hero />
        <LobbyPreview />
        <footer className="site-footer">
          <span>Heads Up Masters</span>
          <span>By Balan Mario.</span>
        </footer>
      </div>
    </BackendHealthGate>
  );
}

export default App;
