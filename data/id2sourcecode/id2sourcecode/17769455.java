    public String md5Encode(String str) {
        byte data[] = str.getBytes();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileMapBackend.class.getName()).log(Level.SEVERE, null, ex);
        }
        digest.update(data);
        byte[] hash = digest.digest();
        String hex = "";
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; ++i) {
            hex = Integer.toHexString((int) hash[i]);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
