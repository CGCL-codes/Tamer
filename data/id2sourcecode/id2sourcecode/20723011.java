    public static String getHash(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        FileInputStream filein = new FileInputStream(file);
        DigestInputStream digest = new DigestInputStream(filein, MessageDigest.getInstance(algorithm));
        byte[] buffer = new byte[8192];
        while (digest.read(buffer) != -1) ;
        digest.close();
        return StringUtils.toHex(digest.getMessageDigest().digest());
    }
