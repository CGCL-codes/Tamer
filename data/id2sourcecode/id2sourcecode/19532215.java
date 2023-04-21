    public static String computeFixity(InputStream in) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(FIXITY_ALG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte buf[] = new byte[8192];
        int n;
        try {
            while ((n = in.read(buf)) > 0) {
                md.update(buf, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        byte hash[] = md.digest();
        return new String(Hex.encodeHex(hash));
    }
