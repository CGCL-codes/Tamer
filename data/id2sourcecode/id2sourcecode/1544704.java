    private byte[] generateKeyBytes(SecretKey pbeKey, byte[] salt, int iterationCount) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] passwd = pbeKey.getEncoded();
        byte[] mD = new byte[64];
        for (int i = 0; i < mD.length; i++) mD[i] = 3;
        byte[] mP = augment(passwd, 64);
        byte[] mS = augment(salt, 64);
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
