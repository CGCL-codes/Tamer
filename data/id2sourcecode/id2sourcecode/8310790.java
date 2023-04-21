    public static boolean validaSenha(String senhaNoBanco, String senhaIntroduzida) throws NoSuchAlgorithmException {
        byte[] b;
        b = EncriptacaoUtil.digest(senhaIntroduzida.getBytes(), "md5");
        String senhaCriptografada = EncriptacaoUtil.byteArrayToHexString(b);
        if (senhaNoBanco.equals(senhaCriptografada)) {
            return true;
        } else {
            return false;
        }
    }
