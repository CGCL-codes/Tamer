    public static byte[] hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(senha.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
