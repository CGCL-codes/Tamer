    public static byte[] hashSHA1(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digester = null;
        FileInputStream streamLectura = null;
        int leidos = 0;
        byte[] buffer = new byte[2048];
        byte[] resultado = null;
        try {
            digester = MessageDigest.getInstance("SHA-1");
            streamLectura = new FileInputStream(file);
            while ((leidos = streamLectura.read(buffer)) != -1) {
                digester.update(buffer, 0, leidos);
            }
            resultado = digester.digest();
        } finally {
            streamLectura.close();
        }
        return resultado;
    }
