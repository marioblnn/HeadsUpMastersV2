import axios from "axios";

export interface Guest {
    displayName:string
    balance:number
    identifier:string
}

export async function pingServer() {
  const response = await axios.get("http://localhost:8080/health");
  if (response.data === "OK"){
    console.log("Server is up")
    return
  }
  console.log("Cannot connect to the server")
}



export async function requestGuest() {
    const response = await axios.get("http://localhost:8090/", {
      withCredentials : true
    })
    console.log(response.data)
    const guest:Guest = response.data
    return guest
}

export async function pingBE() {
    const response = await axios.get("http://localhost:8090/ping")
    console.log(response)
}