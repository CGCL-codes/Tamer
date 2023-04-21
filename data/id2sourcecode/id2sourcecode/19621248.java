    public static final void main(String[] args) throws IOException {
        KeyPair keyPair = AsymmetricCrypto.getInstance().generateKeyPair();
        writeObject(new File(SERVER_PRIVATE_KEY_FILE), keyPair.getPrivate());
        writeObject(new File("PubKey.sjo"), keyPair.getPublic());
    }
