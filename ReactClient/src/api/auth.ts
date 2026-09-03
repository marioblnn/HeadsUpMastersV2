import axios from "axios";
import type { Guest } from "../models/guest";

const GUEST_API = "http://localhost:8090";
const GAME_API = "http://localhost:8090";

export const BACKEND_HEALTH_GUARD_ENABLED = false;

export async function requestGuest(): Promise<Guest> {
  const { data } = await axios.get<Guest>(`${GUEST_API}/`, { withCredentials: true });
  return data;
}

export async function pingGuestServer(): Promise<boolean> {
  try {
    await axios.get(`${GUEST_API}/health`, { timeout: 4000 });
    return true;
  } catch {
    return false;
  }
}

export async function pingGameServer(): Promise<boolean> {
  try {
    const { data } = await axios.get<string>(`${GAME_API}/health`, { timeout: 4000 });
    return data === "OK";
  } catch {
    return false;
  }
}

export async function checkBackendReachability(): Promise<boolean> {
  return (await pingGuestServer()) && (await pingGameServer());
}

export async function waitForBackendAvailability(
  pollMs = 2500,
  signal?: AbortSignal,
): Promise<boolean> {
  if (!BACKEND_HEALTH_GUARD_ENABLED) {
    return true;
  }

  while (!(await checkBackendReachability())) {
    if (signal?.aborted) {
      return false;
    }

    await new Promise<void>((resolve) => {
      const timeoutId = window.setTimeout(resolve, pollMs);
      signal?.addEventListener("abort", () => {
        window.clearTimeout(timeoutId);
        resolve();
      }, { once: true });
    });
  }

  return true;
}