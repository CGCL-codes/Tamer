    private String criptografar(String texto) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException cause) {
            throw new RuntimeException(cause);
        }
        return fromHexaToString(digest.digest(texto.getBytes()));
    }
