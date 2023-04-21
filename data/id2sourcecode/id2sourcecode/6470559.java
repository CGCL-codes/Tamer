    static byte[] makePropDescrRes(int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        if ((objIdx & 0xFFFFFF00) != 0) throw new IllegalArgumentException("'objIdx' out of range!");
        if ((propID & 0xFFFFFF00) != 0) throw new IllegalArgumentException("'propID' out of range!");
        if ((propIdx & 0xFFFFFF00) != 0) throw new IllegalArgumentException("'propIdx' out of range!");
        if ((type & 0xFFFFFF00) != 0) throw new IllegalArgumentException("'type' out of range!");
        if ((maxNoElems & 0xFFFF0000) != 0) throw new IllegalArgumentException("'maxNoElems' out of range!");
        if ((readLevel & 0xFFFFFFF0) != 0) throw new IllegalArgumentException("'readLevel' out of range!");
        if ((writeLevel & 0xFFFFFFF0) != 0) throw new IllegalArgumentException("'writeLevel' out of range!");
        byte[] aPDU = TFrame.getNew(8);
        aPDU[TFrame.APDU_START + 0] = (byte) (APCI.PROPERTYDESCR_RES >> 24);
        aPDU[TFrame.APDU_START + 1] = (byte) (APCI.PROPERTYDESCR_RES >> 16);
        aPDU[TFrame.APDU_START + 2] = (byte) objIdx;
        aPDU[TFrame.APDU_START + 3] = (byte) propID;
        aPDU[TFrame.APDU_START + 4] = (byte) propIdx;
        aPDU[TFrame.APDU_START + 5] = (byte) type;
        aPDU[TFrame.APDU_START + 6] = (byte) (maxNoElems >> 8);
        aPDU[TFrame.APDU_START + 7] = (byte) maxNoElems;
        aPDU[TFrame.APDU_START + 8] = (byte) ((readLevel << 4) | writeLevel);
        return aPDU;
    }
