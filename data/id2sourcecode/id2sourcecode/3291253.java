    private static String makeHash(String hashMethod, String filePath) {
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance(hashMethod);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(hashMethod + " not recognized as Secure Hash Algorithm.", e);
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File " + filePath + " could not be found!", e);
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, hash);
        byte[] b = new byte[1024];
        try {
            while (dis.read(b) >= 0) {
            }
            dis.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read from file '" + filePath + "' while trying ot calculate hash.", e);
        }
        byte[] bytesDigest = dis.getMessageDigest().digest();
        return asHex(bytesDigest);
    }
