    private void calcBase() {
        patk = str + strb;
        matk = ini + inib;
        phit = (dex + dexb) * 2 + luk;
        mhit = 30 + (ini + inib) * 5 + dex + dexb;
        flee = (agi + agib) * 2 + (luk + lukb);
        cri = (dex + dexb) / 2;
        fcri = luk + lukb;
        spd = 5 + agi / 10;
    }
