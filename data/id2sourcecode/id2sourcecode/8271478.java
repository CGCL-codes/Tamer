    public static String GetMD5(String filename) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(filename);
        try {
            is = new DigestInputStream(is, md);
            byte b[] = new byte[1024];
            while (is.available() >= 0) {
                int len = is.read(b);
                if (len < 0) {
                    break;
                }
            }
        } finally {
            is.close();
        }
        return new BigInteger(1, md.digest()).toString(16);
    }
