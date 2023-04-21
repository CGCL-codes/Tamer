    public static byte[] digest(String algorithm, InputStream in) throws IOException, NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[bufferSize];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                md.update(buffer, 0, count);
            }
            return md.digest();
        } finally {
            in.close();
        }
    }
