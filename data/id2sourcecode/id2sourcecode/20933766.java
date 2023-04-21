    public static String criptografar(String senha) {
        if (md != null) {
            return new String(hexCodes(md.digest(senha.getBytes())));
        }
        return null;
    }
