    public static String resumir(String path, String alg) throws Exception {
        try {
            MessageDigest digester = MessageDigest.getInstance(alg);
            byte fileBytes[] = cargarFicherodeDisco(path);
            if (fileBytes != null) {
                byte[] binDigest = digester.digest();
                if (binDigest != null) {
                    return Base64.encode(binDigest);
                }
            }
            return "";
        } catch (Exception e) {
            throw e;
        }
    }
