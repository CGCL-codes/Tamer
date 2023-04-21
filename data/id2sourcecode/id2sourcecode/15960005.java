    public void engineInit(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        PBEBMPKey hmacKey;
        byte[] keyBytes;
        PBEParameterSpec spec;
        if (key instanceof PBEBMPKey) {
            hmacKey = (PBEBMPKey) key;
            if (params instanceof PBEParameterSpec) {
                spec = (PBEParameterSpec) params;
                keyBytes = generateKeyBytesPKCS12(hmacKey, spec.getSalt(), spec.getIterationCount());
            } else throw new InvalidAlgorithmParameterException("Unsupported AlgorithmParameterSpec");
        } else if (key instanceof SecretKey) {
            keyBytes = key.getEncoded();
        } else throw new InvalidKeyException();
        int n = B_ - keyBytes.length;
        if (n > 0) {
            System.arraycopy(keyBytes, 0, macKey, 0, keyBytes.length);
            for (int i = 0; i < n; i++) macKey[keyBytes.length + i] = 0;
        } else {
            if (n < 0) {
                md.update(keyBytes);
                macKey = md.digest();
            }
        }
        System.arraycopy(macKey, 0, ipad_key, 0, B_);
        System.arraycopy(macKey, 0, opad_key, 0, B_);
        for (int j = 0; j < B_; j++) {
            ipad_key[j] = (byte) (ipad_key[j] ^ ipad_);
            opad_key[j] = (byte) (opad_key[j] ^ opad_);
        }
        md.update(ipad_key);
    }
