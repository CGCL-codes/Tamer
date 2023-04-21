    private void sendMTFValues() throws IOException {
        char len[][] = new char[N_GROUPS][MAX_ALPHA_SIZE];
        int v, t, i, j, gs, ge, totc, bt, bc, iter;
        int nSelectors = 0, alphaSize, minLen, maxLen, selCtr;
        int nGroups, nBytes;
        alphaSize = nInUse + 2;
        for (t = 0; t < N_GROUPS; t++) {
            for (v = 0; v < alphaSize; v++) {
                len[t][v] = (char) GREATER_ICOST;
            }
        }
        if (nMTF <= 0) {
            panic();
        }
        if (nMTF < 200) {
            nGroups = 2;
        } else if (nMTF < 600) {
            nGroups = 3;
        } else if (nMTF < 1200) {
            nGroups = 4;
        } else if (nMTF < 2400) {
            nGroups = 5;
        } else {
            nGroups = 6;
        }
        {
            int nPart, remF, tFreq, aFreq;
            nPart = nGroups;
            remF = nMTF;
            gs = 0;
            while (nPart > 0) {
                tFreq = remF / nPart;
                ge = gs - 1;
                aFreq = 0;
                while (aFreq < tFreq && ge < alphaSize - 1) {
                    ge++;
                    aFreq += mtfFreq[ge];
                }
                if (ge > gs && nPart != nGroups && nPart != 1 && ((nGroups - nPart) % 2 == 1)) {
                    aFreq -= mtfFreq[ge];
                    ge--;
                }
                for (v = 0; v < alphaSize; v++) {
                    if (v >= gs && v <= ge) {
                        len[nPart - 1][v] = (char) LESSER_ICOST;
                    } else {
                        len[nPart - 1][v] = (char) GREATER_ICOST;
                    }
                }
                nPart--;
                gs = ge + 1;
                remF -= aFreq;
            }
        }
        int[][] rfreq = new int[N_GROUPS][MAX_ALPHA_SIZE];
        int[] fave = new int[N_GROUPS];
        short[] cost = new short[N_GROUPS];
        for (iter = 0; iter < N_ITERS; iter++) {
            for (t = 0; t < nGroups; t++) {
                fave[t] = 0;
            }
            for (t = 0; t < nGroups; t++) {
                for (v = 0; v < alphaSize; v++) {
                    rfreq[t][v] = 0;
                }
            }
            nSelectors = 0;
            totc = 0;
            gs = 0;
            while (true) {
                if (gs >= nMTF) {
                    break;
                }
                ge = gs + G_SIZE - 1;
                if (ge >= nMTF) {
                    ge = nMTF - 1;
                }
                for (t = 0; t < nGroups; t++) {
                    cost[t] = 0;
                }
                if (nGroups == 6) {
                    short cost0, cost1, cost2, cost3, cost4, cost5;
                    cost0 = cost1 = cost2 = cost3 = cost4 = cost5 = 0;
                    for (i = gs; i <= ge; i++) {
                        short icv = szptr[i];
                        cost0 += len[0][icv];
                        cost1 += len[1][icv];
                        cost2 += len[2][icv];
                        cost3 += len[3][icv];
                        cost4 += len[4][icv];
                        cost5 += len[5][icv];
                    }
                    cost[0] = cost0;
                    cost[1] = cost1;
                    cost[2] = cost2;
                    cost[3] = cost3;
                    cost[4] = cost4;
                    cost[5] = cost5;
                } else {
                    for (i = gs; i <= ge; i++) {
                        short icv = szptr[i];
                        for (t = 0; t < nGroups; t++) {
                            cost[t] += len[t][icv];
                        }
                    }
                }
                bc = 999999999;
                bt = -1;
                for (t = 0; t < nGroups; t++) {
                    if (cost[t] < bc) {
                        bc = cost[t];
                        bt = t;
                    }
                }
                ;
                totc += bc;
                fave[bt]++;
                selector[nSelectors] = (char) bt;
                nSelectors++;
                for (i = gs; i <= ge; i++) {
                    rfreq[bt][szptr[i]]++;
                }
                gs = ge + 1;
            }
            for (t = 0; t < nGroups; t++) {
                hbMakeCodeLengths(len[t], rfreq[t], alphaSize, 20);
            }
        }
        rfreq = null;
        fave = null;
        cost = null;
        if (!(nGroups < 8)) {
            panic();
        }
        if (!(nSelectors < 32768 && nSelectors <= (2 + (900000 / G_SIZE)))) {
            panic();
        }
        {
            char[] pos = new char[N_GROUPS];
            char ll_i, tmp2, tmp;
            for (i = 0; i < nGroups; i++) {
                pos[i] = (char) i;
            }
            for (i = 0; i < nSelectors; i++) {
                ll_i = selector[i];
                j = 0;
                tmp = pos[j];
                while (ll_i != tmp) {
                    j++;
                    tmp2 = tmp;
                    tmp = pos[j];
                    pos[j] = tmp2;
                }
                pos[0] = tmp;
                selectorMtf[i] = (char) j;
            }
        }
        int[][] code = new int[N_GROUPS][MAX_ALPHA_SIZE];
        for (t = 0; t < nGroups; t++) {
            minLen = 32;
            maxLen = 0;
            for (i = 0; i < alphaSize; i++) {
                if (len[t][i] > maxLen) {
                    maxLen = len[t][i];
                }
                if (len[t][i] < minLen) {
                    minLen = len[t][i];
                }
            }
            if (maxLen > 20) {
                panic();
            }
            if (minLen < 1) {
                panic();
            }
            hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
        }
        {
            boolean[] inUse16 = new boolean[16];
            for (i = 0; i < 16; i++) {
                inUse16[i] = false;
                for (j = 0; j < 16; j++) {
                    if (inUse[i * 16 + j]) {
                        inUse16[i] = true;
                    }
                }
            }
            nBytes = bytesOut;
            for (i = 0; i < 16; i++) {
                if (inUse16[i]) {
                    bsW(1, 1);
                } else {
                    bsW(1, 0);
                }
            }
            for (i = 0; i < 16; i++) {
                if (inUse16[i]) {
                    for (j = 0; j < 16; j++) {
                        if (inUse[i * 16 + j]) {
                            bsW(1, 1);
                        } else {
                            bsW(1, 0);
                        }
                    }
                }
            }
        }
        nBytes = bytesOut;
        bsW(3, nGroups);
        bsW(15, nSelectors);
        for (i = 0; i < nSelectors; i++) {
            for (j = 0; j < selectorMtf[i]; j++) {
                bsW(1, 1);
            }
            bsW(1, 0);
        }
        nBytes = bytesOut;
        for (t = 0; t < nGroups; t++) {
            int curr = len[t][0];
            bsW(5, curr);
            for (i = 0; i < alphaSize; i++) {
                while (curr < len[t][i]) {
                    bsW(2, 2);
                    curr++;
                }
                while (curr > len[t][i]) {
                    bsW(2, 3);
                    curr--;
                }
                bsW(1, 0);
            }
        }
        nBytes = bytesOut;
        selCtr = 0;
        gs = 0;
        while (true) {
            if (gs >= nMTF) {
                break;
            }
            ge = gs + G_SIZE - 1;
            if (ge >= nMTF) {
                ge = nMTF - 1;
            }
            for (i = gs; i <= ge; i++) {
                bsW(len[selector[selCtr]][szptr[i]], code[selector[selCtr]][szptr[i]]);
            }
            gs = ge + 1;
            selCtr++;
        }
        if (!(selCtr == nSelectors)) {
            panic();
        }
    }
