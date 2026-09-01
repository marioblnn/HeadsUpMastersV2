import { BackendHealthGate } from "./components/BackendHealthGate";
import { Header } from "./components/PokerHeader";
import { Hero } from "./components/PokerHero";
import { LobbyPreview } from "./components/LobbyPreview";

function App() {
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
