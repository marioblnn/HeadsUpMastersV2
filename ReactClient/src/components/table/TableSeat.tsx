import { UserRound } from "lucide-react";

type TableSeatProps = {
  position: "top" | "bottom";
  onJoin: () => void;
};

export function TableSeat({ position, onJoin }: TableSeatProps) {
  return (
    <button
      className={`table-seat table-seat--${position}`}
      type="button"
      onClick={onJoin}
      aria-label={`Join the ${position} seat`}
    >
      <span className="table-seat__icon" aria-hidden="true">
        <UserRound size={18} />
      </span>
      <span className="table-seat__copy">
        <strong>Open seat</strong>
        <small>Click to join</small>
      </span>
    </button>
  );
}
