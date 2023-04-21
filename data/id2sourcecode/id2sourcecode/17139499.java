    private static String getHash(byte[] b) {
        MessageDigest md = localDigest.get();
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            localDigest.set(md);
        }
        md.reset();
        md.update(b);
        byte[] hash = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toHexString((hash[i] >> 4) & 0x0F));
            sb.append(Integer.toHexString(hash[i] & 0x0F));
        }
        String rv = sb.toString();
        return rv;
    }
