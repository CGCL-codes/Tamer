    private static synchronized String getMessageDiggetString(File file, String algorithm) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        if (file.exists() == false || file.canRead() == false) {
            return null;
        }
        MessageDigest md = MessageDigest.getInstance(algorithm);
        String hash = "";
        int bufferSize = 1024;
        if (file.length() < Integer.MAX_VALUE) bufferSize = (int) file.length(); else bufferSize = Integer.MAX_VALUE;
        byte[] data = new byte[bufferSize];
        FileInputStream fis = new FileInputStream(file);
        for (int n = 0; (n = fis.read(data)) > -1; ) md.update(data, 0, n);
        fis.close();
        byte[] digest = md.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(digest[i]);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hex = hex.substring(hex.length() - 2);
            hash += hex;
        }
        return hash;
    }
