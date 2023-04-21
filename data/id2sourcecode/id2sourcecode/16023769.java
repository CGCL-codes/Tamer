    protected byte[] engineWrap(byte[] in, int inOffset, int length) {
        if (length != 16 && length != 24) throw new IllegalArgumentException("Only 2- and 3-key Triple DES keys are alowed");
        byte[] CEK = new byte[24];
        if (length == 16) {
            System.arraycopy(in, inOffset, CEK, 0, 16);
            System.arraycopy(in, inOffset, CEK, 16, 8);
        } else System.arraycopy(in, inOffset, CEK, 0, 24);
        TripleDES.adjustParity(CEK, 0);
        sha.update(CEK);
        byte[] hash = sha.digest();
        byte[] ICV = new byte[8];
        System.arraycopy(hash, 0, ICV, 0, 8);
        byte[] CEKICV = new byte[CEK.length + ICV.length];
        System.arraycopy(CEK, 0, CEKICV, 0, CEK.length);
        System.arraycopy(ICV, 0, CEKICV, CEK.length, ICV.length);
        byte[] IV = new byte[8];
        nextRandomBytes(IV);
        modeAttributes.put(IMode.IV, IV);
        asmAttributes.put(Assembly.DIRECTION, Direction.FORWARD);
        byte[] TEMP1;
        try {
            asm.init(asmAttributes);
            TEMP1 = asm.lastUpdate(CEKICV);
        } catch (TransformerException x) {
            throw new RuntimeException(x);
        }
        byte[] TEMP2 = new byte[IV.length + TEMP1.length];
        System.arraycopy(IV, 0, TEMP2, 0, IV.length);
        System.arraycopy(TEMP1, 0, TEMP2, IV.length, TEMP1.length);
        byte[] TEMP3 = new byte[TEMP2.length];
        for (int i = 0, j = TEMP2.length - 1; i < TEMP2.length; i++, j--) TEMP3[j] = TEMP2[i];
        modeAttributes.put(IMode.IV, DEFAULT_IV);
        asmAttributes.put(Assembly.DIRECTION, Direction.FORWARD);
        byte[] result;
        try {
            asm.init(asmAttributes);
            result = asm.lastUpdate(TEMP3);
        } catch (TransformerException x) {
            throw new RuntimeException(x);
        }
        return result;
    }
