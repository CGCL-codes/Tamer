    public static String getDigest(final File file, final String algorithm) throws NoSuchAlgorithmException {
        StringBuffer result = new StringBuffer(32);
        Formatter f = new Formatter(result);
        MessageDigest messageDigest = null;
        try {
            byte[] md = new byte[8192];
            FileInputStream in = new FileInputStream(file);
            messageDigest = MessageDigest.getInstance(algorithm);
            for (int n = 0; (n = in.read(md)) > 0; ) {
                messageDigest.update(md, 0, n);
            }
            for (byte b : messageDigest.digest()) {
                f.format("%02x", b);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
