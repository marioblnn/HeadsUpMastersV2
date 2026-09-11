import { Plus } from "lucide-react";

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
      <Plus size={36} strokeWidth={1.8} aria-hidden="true" />
    </button>
  );
}
