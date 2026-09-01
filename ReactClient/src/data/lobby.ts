export type LobbyTableColor = "purple" | "peach" | "mint" | "orange";

export type LobbyTable = {
  id: number;
  name: string;
  stakes: string;
  minBuyIn: string;
  maxBuyIn: string;
  players: string;
  dealer: string;
  color: LobbyTableColor;
  occupancy: 0 | 0.5 | 1;
};

export const lobbyTables: LobbyTable[] = [
  {
    id: 1,
    name: "NLHE Live Table #3",
    stakes: "$1 / $2",
    minBuyIn: "$20",
    maxBuyIn: "$200",
    players: "0/2",
    dealer: "Kevin",
    color: "purple",
    occupancy: 0,
  },
  {
    id: 2,
    name: "NLHE Live Table #3",
    stakes: "$1 / $2",
    minBuyIn: "$20",
    maxBuyIn: "$200",
    players: "1/2",
    dealer: "Kate",
    color: "peach",
    occupancy: 0.5,
  },
  {
    id: 3,
    name: "Omaha Live Table #11",
    stakes: "$1 / $2",
    minBuyIn: "$20",
    maxBuyIn: "$200",
    players: "2/2",
    dealer: "Sophie",
    color: "mint",
    occupancy: 1,
  },
  {
    id: 4,
    name: "NLHE Live Table #3",
    stakes: "$1 / $2",
    minBuyIn: "$20",
    maxBuyIn: "$200",
    players: "1/2",
    dealer: "Alex",
    color: "orange",
    occupancy: 0.5,
  },
  {
    id: 5,
    name: "PLO Live Table #8",
    stakes: "$2 / $5",
    minBuyIn: "$50",
    maxBuyIn: "$500",
    players: "2/6",
    dealer: "Mia",
    color: "purple",
    occupancy: 0.5,
  },
  {
    id: 6,
    name: "NLHE Fast Table #14",
    stakes: "$5 / $10",
    minBuyIn: "$100",
    maxBuyIn: "$1,000",
    players: "0/9",
    dealer: "Leo",
    color: "mint",
    occupancy: 0,
  },
  {
    id: 7,
    name: "Omaha Hi-Lo Table #2",
    stakes: "$2 / $5",
    minBuyIn: "$40",
    maxBuyIn: "$400",
    players: "6/6",
    dealer: "Nina",
    color: "orange",
    occupancy: 1,
  },
  {
    id: 8,
    name: "NLHE Live Table #9",
    stakes: "$1 / $3",
    minBuyIn: "$30",
    maxBuyIn: "$300",
    players: "4/6",
    dealer: "Harper",
    color: "peach",
    occupancy: 0.5,
  },
  {
    id: 9,
    name: "Short Deck Table #5",
    stakes: "$3 / $6",
    minBuyIn: "$75",
    maxBuyIn: "$750",
    players: "3/6",
    dealer: "Riley",
    color: "purple",
    occupancy: 0.5,
  },
  {
    id: 10,
    name: "NLHE Turbo Table #17",
    stakes: "$10 / $25",
    minBuyIn: "$200",
    maxBuyIn: "$2,000",
    players: "1/8",
    dealer: "Jordan",
    color: "mint",
    occupancy: 0.5,
  },
];
