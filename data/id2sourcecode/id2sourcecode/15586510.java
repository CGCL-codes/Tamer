    public PrivateKey getPrivateKey() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        PrincipalKeyDirectory privateKeyDirectory = privateKeyDirectory(_groupManager.getAccessManager());
        PrivateKey privateKey = privateKeyDirectory.getPrivateKey();
        if (null != privateKey) {
            _handle.keyManager().getSecureKeyCache().addPrivateKey(privateKeyDirectory.getPrivateKeyBlockName(), publicKeyObject().publicKeyDigest().digest(), privateKey);
        }
        return privateKey;
    }
