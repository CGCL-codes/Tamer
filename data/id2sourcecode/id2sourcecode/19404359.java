    public static byte[] makeHashing(InputStream inputStream, String algName) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = null;
        byte[] buffer = new byte[1024];
        md = MessageDigest.getInstance(algName);
        int n;
        while ((n = inputStream.read(buffer)) != -1) {
            md.update(buffer, 0, n);
        }
        inputStream.close();
        byte raw[] = md.digest();
        return raw;
    }
