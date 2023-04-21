    private byte[] generateKeyBytesPKCS12(PBEBMPKey hmacKey, byte[] salt, int iterationCount) {
        byte[] passwd = hmacKey.getEncoded();
        byte[] mD = new byte[64];
        for (int i = 0; i < mD.length; i++) mD[i] = 3;
        byte[] mP = augment(passwd);
        byte[] mS = augment(salt);
        byte[] mI = new byte[mP.length + mS.length];
        System.arraycopy(mS, 0, mI, 0, mS.length);
        System.arraycopy(mP, 0, mI, mS.length, mP.length);
        byte[] mA;
        md.update(mD);
        md.update(mI);
        mA = md.digest();
        for (int i = 1; i < iterationCount; i++) {
            md.update(mA);
            mA = md.digest();
        }
        return mA;
    }
