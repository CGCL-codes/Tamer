    public static byte[] getFileHash(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] data = new byte[1024];
        int bytesRead;
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        while ((bytesRead = fis.read(data)) > 0) {
            hash.update(data, 0, bytesRead);
        }
        return hash.digest();
    }
