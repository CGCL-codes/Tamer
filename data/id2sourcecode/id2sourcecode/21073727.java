    public static byte[] doHashSHA(char[] password) {
        try {
            byte[] buffer = new String(password).getBytes("UTF8");
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(buffer);
            CryptoUtils.clear(password);
            CryptoUtils.clear(buffer);
            return md.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
