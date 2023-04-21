    private byte[] getHash(String nomeArquivo, String metodo) {
        MessageDigest md = null;
        FileInputStream fis = null;
        try {
            md = MessageDigest.getInstance(metodo);
            byte[] buffer = new byte[4096];
            fis = new FileInputStream(nomeArquivo);
            int bytesLidos = -1;
            while ((bytesLidos = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesLidos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }
        return md.digest();
    }
