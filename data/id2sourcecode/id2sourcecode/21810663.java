    public int propertyDescr_ReadRes(int da, int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel, boolean waitL2Con) {
        byte[] aPDU = APDU.makePropDescrRes(objIdx, propID, propIdx, type, maxNoElems, readLevel, writeLevel);
        return tus.dataUnack_Req(da, pr, hc, aPDU, waitL2Con);
    }
