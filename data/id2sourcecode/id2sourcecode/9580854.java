    public String createRSAKey(String rsaKeyDirectory) {
        String uuid = (new UID()).toString();
        uuid = uuid.replace(':', '_');
        String filename = rsaKeyDirectory + uuid;
        String alg = "RSA";
        int bits = 1024;
        String result = "";
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(alg);
            kpg.initialize(bits, SSHInteractiveClient.secureRandom());
            keyPair = kpg.generateKeyPair();
            try {
                result = storeKeyPair(keyPair, filename);
            } catch (Exception e) {
                logger.log(Level.WARNING, "[SshImplMindTerm] Error in storing the keypair: " + e.getMessage() + " " + e.getStackTrace());
            }
        } catch (NoSuchAlgorithmException algException) {
            logger.log(Level.WARNING, "[SshImplMindTerm] Error in rsa key algorithm.");
        }
        return result;
    }
