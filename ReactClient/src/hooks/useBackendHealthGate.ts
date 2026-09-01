import { useEffect, useState } from "react";
import { BACKEND_HEALTH_GUARD_ENABLED, checkBackendReachability } from "../api/auth";

const DEFAULT_RETRY_DELAY_MS = 2500;

export function useBackendHealthGate() {
  const [isBackendReady, setIsBackendReady] = useState(!BACKEND_HEALTH_GUARD_ENABLED);

  useEffect(() => {
    if (!BACKEND_HEALTH_GUARD_ENABLED) {
      return;
    }

    let isMounted = true;
    let timeoutId: number | undefined;

    const pollBackend = async () => {
      const reachable = await checkBackendReachability();

      if (!isMounted) {
        return;
      }

      setIsBackendReady(reachable);

      if (!reachable) {
        timeoutId = window.setTimeout(pollBackend, DEFAULT_RETRY_DELAY_MS);
      }
    };

    void pollBackend();

    return () => {
      isMounted = false;
      if (timeoutId) {
        window.clearTimeout(timeoutId);
      }
    };
  }, []);

  return isBackendReady;
}
