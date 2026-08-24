import { useEffect, useState } from "react";
import { pingBE} from "./api/auth";
import { requestGuest } from "./api/auth";

export interface Guest {
    displayName:string
    balance:number
}


function App() {
  const [displayName, setDisplayName] = useState("Null")
  const [balance, setBalance] = useState(0)

  useEffect(() => {
  async function getGuestData(){
    try {
      const guest:Guest = await requestGuest()
      setDisplayName(guest.displayName)
      setBalance(guest.balance)
    } catch (error){
      console.log("Failed to fetch an guest: ", error)
    }
  }
  getGuestData()
}, []); 

  return (
    <>
    <h1>Welcome back {displayName} !</h1>
    <h1>balance {balance}</h1>
    <button className="w-20 h-10 bg-green-600" onClick={pingBE}>ping action</button>
    </>
  );
}

export default App;