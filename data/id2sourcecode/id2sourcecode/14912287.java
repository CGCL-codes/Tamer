    public static String getMD5(File file) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found " + file.getName());
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        updateMessageDigest(file, md);
        byte[] hash = md.digest();
        BigInteger result = new BigInteger(hash);
        String rc = result.toString(16);
        return rc;
    }
