    protected TlsBlockCipherCipherSuite(BlockCipher encrypt, BlockCipher decrypt, Digest writeDigest, Digest readDigest, int cipherKeySize, short keyExchange) {
        this.encryptCipher = encrypt;
        this.decryptCipher = decrypt;
        this.writeDigest = writeDigest;
        this.readDigest = readDigest;
        this.cipherKeySize = cipherKeySize;
        this.keyExchange = keyExchange;
    }
