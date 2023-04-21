    void init(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException("HMAC does not use parameters");
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Secret key expected");
        }
        byte[] secret = key.getEncoded();
        if (secret == null) {
            throw new InvalidKeyException("Missing key data");
        }
        if (secret.length > blockLen) {
            byte[] tmp = md.digest(secret);
            Arrays.fill(secret, (byte) 0);
            secret = tmp;
        }
        for (int i = 0; i < blockLen; i++) {
            int si = (i < secret.length) ? secret[i] : 0;
            k_ipad[i] = (byte) (si ^ 0x36);
            k_opad[i] = (byte) (si ^ 0x5c);
        }
        Arrays.fill(secret, (byte) 0);
        secret = null;
        reset();
    }
