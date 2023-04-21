    public static void main(String[] args) {
        try {
            String senha = JOptionPane.showInputDialog("Digite uma senha:");
            BigInteger hash = new BigInteger(1, CriptoUtils.digest(senha.getBytes(), "MD5"));
            String saida = "Entrada: " + senha + "\nSenha com MD5: " + hash.toString(16);
            JOptionPane.showConfirmDialog(null, saida + "\nTamanho: " + senha.length() + "\ninteiro: " + hash.toString(), "Resultado", JOptionPane.CLOSED_OPTION);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
