    protected byte[] engineUnwrap(byte[] in, int inOffset, int length) throws KeyUnwrappingException {
        if (length != 40) throw new IllegalArgumentException("length MUST be 40");
        modeAttributes.put(IMode.IV, DEFAULT_IV);
        asmAttributes.put(Assembly.DIRECTION, Direction.REVERSED);
        byte[] TEMP3;
        try {
            asm.init(asmAttributes);
            TEMP3 = asm.lastUpdate(in, inOffset, 40);
        } catch (TransformerException x) {
            throw new RuntimeException(x);
        }
        byte[] TEMP2 = new byte[40];
        for (int i = 0, j = 40 - 1; i < 40; i++, j--) TEMP2[j] = TEMP3[i];
        byte[] IV = new byte[8];
        byte[] TEMP1 = new byte[32];
        System.arraycopy(TEMP2, 0, IV, 0, 8);
        System.arraycopy(TEMP2, 8, TEMP1, 0, 32);
        modeAttributes.put(IMode.IV, IV);
        asmAttributes.put(Assembly.DIRECTION, Direction.REVERSED);
        byte[] CEKICV;
        try {
            asm.init(asmAttributes);
            CEKICV = asm.lastUpdate(TEMP1, 0, 32);
        } catch (TransformerException x) {
            throw new RuntimeException(x);
        }
        byte[] CEK = new byte[24];
        byte[] ICV = new byte[8];
        System.arraycopy(CEKICV, 0, CEK, 0, 24);
        System.arraycopy(CEKICV, 24, ICV, 0, 8);
        sha.update(CEK);
        byte[] hash = sha.digest();
        byte[] computedICV = new byte[8];
        System.arraycopy(hash, 0, computedICV, 0, 8);
        if (!Arrays.equals(ICV, computedICV)) throw new KeyUnwrappingException("ICV and computed ICV MUST match");
        if (!TripleDES.isParityAdjusted(CEK, 0)) throw new KeyUnwrappingException("Triple-DES key parity MUST be adjusted");
        return CEK;
    }
