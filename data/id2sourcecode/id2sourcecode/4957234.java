    public static boolean validaSenha(String senha) throws NoSuchAlgorithmException {
        String senhaNoBanco = "";
        byte[] b;
        try {
            b = CriptoUtils.digest(senha.getBytes(), "md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        String senhaCriptografada = CriptoUtils.byteArrayToHexString(b);
        if (senhaNoBanco.equalsIgnoreCase(senhaCriptografada)) {
            return true;
        } else {
            return false;
        }
    }
