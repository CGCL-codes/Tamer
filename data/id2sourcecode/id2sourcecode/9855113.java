    public byte[] protect(Key key) throws KeyStoreException {
        int i;
        int numRounds;
        byte[] digest;
        int xorOffset;
        int encrKeyOffset = 0;
        if (key == null) {
            throw new IllegalArgumentException("plaintext key can't be null");
        }
        byte[] plainKey = key.getEncoded();
        numRounds = plainKey.length / DIGEST_LEN;
        if ((plainKey.length % DIGEST_LEN) != 0) numRounds++;
        byte[] salt = new byte[SALT_LEN];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        byte[] xorKey = new byte[plainKey.length];
        for (i = 0, xorOffset = 0, digest = salt; i < numRounds; i++, xorOffset += DIGEST_LEN) {
            md.update(passwdBytes);
            md.update(digest);
            digest = md.digest();
            md.reset();
            if (i < numRounds - 1) {
                System.arraycopy(digest, 0, xorKey, xorOffset, digest.length);
            } else {
                System.arraycopy(digest, 0, xorKey, xorOffset, xorKey.length - xorOffset);
            }
        }
        byte[] tmpKey = new byte[plainKey.length];
        for (i = 0; i < tmpKey.length; i++) {
            tmpKey[i] = (byte) (plainKey[i] ^ xorKey[i]);
        }
        byte[] encrKey = new byte[salt.length + tmpKey.length + DIGEST_LEN];
        System.arraycopy(salt, 0, encrKey, encrKeyOffset, salt.length);
        encrKeyOffset += salt.length;
        System.arraycopy(tmpKey, 0, encrKey, encrKeyOffset, tmpKey.length);
        encrKeyOffset += tmpKey.length;
        md.update(passwdBytes);
        Arrays.fill(passwdBytes, (byte) 0x00);
        passwdBytes = null;
        md.update(plainKey);
        digest = md.digest();
        md.reset();
        System.arraycopy(digest, 0, encrKey, encrKeyOffset, digest.length);
        AlgorithmId encrAlg;
        try {
            encrAlg = new AlgorithmId(new ObjectIdentifier(KEY_PROTECTOR_OID));
            return new EncryptedPrivateKeyInfo(encrAlg, encrKey).getEncoded();
        } catch (IOException ioe) {
            throw new KeyStoreException(ioe.getMessage());
        }
    }
