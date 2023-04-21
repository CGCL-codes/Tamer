    private static String md5(String str) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        byte[] bs = digest.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bs.length; i++) {
            sb.append(Integer.toString((bs[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
