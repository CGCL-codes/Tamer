    private static String calculateHash(String url) {
        if (url == null || url.isEmpty()) return null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.getInstance().error("Fail to calculate hash", e);
        }
        if (md == null) return null;
        byte[] bytes = md.digest(url.getBytes());
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            hash.append(Integer.toHexString((0x000000FF & bytes[i]) | 0xFFFFFF00).substring(6));
        }
        return hash.toString();
    }
