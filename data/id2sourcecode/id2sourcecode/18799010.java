    private String md5(String str) {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AuthorizationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        byte[] bs = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bs.length; i++) sb.append(Integer.toString((bs[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }
