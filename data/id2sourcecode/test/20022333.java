    protected byte[] generateKeyBytesPKCS12(PBEBMPKey pbeKey, byte[] salt, int iterationCount, int keyLength, int id) {
        byte[] passwd = pbeKey.getEncoded();
        byte[] mD = new byte[64];
        for (int i = 0; i < mD.length; i++) mD[i] = (byte) id;
        byte[] mP = augment(passwd);
        byte[] mS = augment(salt);
        byte[] mI = new byte[mP.length + mS.length];
        System.arraycopy(mS, 0, mI, 0, mS.length);
        System.arraycopy(mP, 0, mI, mS.length, mP.length);
        byte[] mA;
        byte[] outCut = new byte[keyLength];
        int k = 1;
        do {
            messageDigest_.update(mD);
            messageDigest_.update(mI);
            mA = messageDigest_.digest();
            for (int i = 1; i < iterationCount; i++) {
                messageDigest_.update(mA);
                mA = messageDigest_.digest();
            }
            if (keyLength < mA.length) {
                System.arraycopy(mA, 0, outCut, 0, keyLength);
                break;
            } else if ((keyLength < k * mA.length) || (keyLength == k * mA.length)) {
                int rem = mA.length - (k * mA.length - keyLength);
                System.arraycopy(mA, 0, outCut, (k - 1) * mA.length, rem);
                break;
            } else System.arraycopy(mA, 0, outCut, (k - 1) * mA.length, mA.length);
            byte[] mB = augment(mA);
            BigInteger b = new BigInteger(mB);
            byte[] ij = new byte[64];
            byte[] modByte = new byte[65];
            byte[] one = { 1 };
            System.arraycopy(one, 0, modByte, 0, 1);
            BigInteger modulo = new BigInteger(modByte);
            BigInteger tmp = null;
            for (int j = 0; j < mI.length / 64; j++) {
                System.arraycopy(mI, j * 64, ij, 0, 64);
                BigInteger ivint = new BigInteger(ij);
                tmp = ((b.add(ivint)).add(b.ONE)).mod(modulo);
                byte[] tmp1 = tmp.toByteArray();
                System.arraycopy(tmp1, tmp1.length - 64, mI, j * 64, 64);
            }
            k++;
        } while ((k - 1) * mA.length < keyLength);
        return outCut;
    }
