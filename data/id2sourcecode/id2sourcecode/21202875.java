    public int propertyDescr_ReadRes(int pr, int hc, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        byte[] aPDU = APDU.makePropDescrRes(objIdx, propID, propIdx, type, maxNoElems, readLevel, writeLevel);
        return tc.data_Req(pr, hc, aPDU);
    }
