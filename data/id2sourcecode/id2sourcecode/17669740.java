    public OutputStream encrypt(OutputStream outputStream) throws Exception {
        if (key == null) {
            key = generateKey(getKeysize());
            pbeIV = randomBytes(PBE_IV_LENGTH);
            encryptionIV = randomBytes(ENCRYPTION_IV_LENGTH);
            encryptedKeyBytes = transformWithPassword(key.getEncoded(), pbeIV, password, Cipher.ENCRYPT_MODE);
        }
        outputStream.write(pbeIV);
        outputStream.write(encryptionIV);
        outputStream.write(encryptedKeyBytes.length);
        outputStream.write(encryptedKeyBytes);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(encryptionIV));
        outputStream = new FilterOutputStream(outputStream) {

            @Override
            public void close() throws IOException {
            }
        };
        return new CipherOutputStream(outputStream, cipher);
    }
