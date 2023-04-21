    public EncryptedMessageWriter(EMSExceptionHandler exceptionHandler, MessageWriter messageWriter, String algorithm, int maximumBlockSize, PublicKey encryptionKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        this.exceptionHandler = exceptionHandler;
        writer = messageWriter;
        key = encryptionKey;
        maxBlockSize = maximumBlockSize;
        cipher = Cipher.getInstance(algorithm);
        if (key != null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
    }
