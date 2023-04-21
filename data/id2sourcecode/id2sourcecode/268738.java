    private static int modInv(int NbrMod, int currentPrime) {
        int QQ, T1, T3;
        int V1 = 1;
        int V3 = NbrMod;
        int U1 = 0;
        int U3 = currentPrime;
        while (V3 != 0) {
            if (U3 < V3 + V3) {
                T1 = U1 - V1;
                T3 = U3 - V3;
            } else {
                QQ = U3 / V3;
                T1 = U1 - V1 * QQ;
                T3 = U3 - V3 * QQ;
            }
            U1 = V1;
            U3 = V3;
            V1 = T1;
            V3 = T3;
        }
        return U1 + (currentPrime & (U1 >> 31));
    }
