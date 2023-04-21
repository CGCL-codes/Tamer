    private static byte[] digest(InputStream in, String algorithm) throws NoSuchAlgorithmException, IOException {
        byte[] buffer = new byte[4096];
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        int bytes = 0;
        while ((bytes = in.read(buffer)) > -1) {
            digest.update(buffer, 0, bytes);
        }
        in.close();
        return digest.digest();
    }
