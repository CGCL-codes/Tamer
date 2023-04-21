    protected byte[] generateKeyBytes(PBEKey pbeKey, byte[] salt, int iterationCount, int keyLength) {
        byte[] out;
        byte[] outCut = new byte[keyLength];
        messageDigest_.update(pbeKey.getEncoded());
        messageDigest_.update(salt);
        out = messageDigest_.digest();
        for (int i = 1; i < iterationCount; i++) {
            messageDigest_.update(out);
            out = messageDigest_.digest();
        }
        System.arraycopy(out, 0, outCut, 0, keyLength);
        return outCut;
    }
