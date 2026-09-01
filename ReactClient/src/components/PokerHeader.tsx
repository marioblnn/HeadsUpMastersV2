import { ChevronDown, CircleHelp, UserRound } from "lucide-react";
import logo from "/assets/images/logo.png";


function BrandMark() {
  return (
    <a className="brand-mark" href="/" aria-label="Heads Up Masters home">
      <img className="brand-mark__icon" src={logo} alt="" />
      <span className="brand-mark__name">
        Heads Up Masters v2
        <span className="brand-mark__byline">by Mario Balan</span>
      </span>
    </a>
  );
}

function AccountWidget() {
  return (
    <div className="account-widget" aria-label="Signed in as Mario, balance one thousand dollars">
      <div className="account-widget__identity">
        <span className="account-widget__avatar" aria-hidden="true">
          <UserRound size={15} />
        </span>
        <span className="account-widget__name">Mario</span>
        <ChevronDown className="account-widget__chevron" size={14} aria-hidden="true" />
      </div>
      <div className="account-widget__balance">
        <span className="account-widget__balance-label">Balance</span>
        <strong>$1,000.00</strong>
      </div>
    </div>
  );
}

export function Header() {
  return (
    <header className="site-header">
      <div className="site-header__inner">
        <BrandMark />
        <nav className="site-nav" aria-label="Main navigation">
          <a href="#how-to-play">How to play</a>
          <a href="#help" aria-label="Help">
            <CircleHelp size={17} />
          </a>
        </nav>
        <AccountWidget />
      </div>
    </header>
  );
}
