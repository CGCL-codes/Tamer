    public static String md5Hex(byte[] blob) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(blob);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("SSH2KeyFingerprint.md5Hex: " + e);
        }
        byte[] raw = md5.digest();
        String hex = HexDump.toString(raw);
        StringBuffer fps = new StringBuffer();
        for (int i = 0; i < hex.length(); i += 2) {
            fps.append(hex.substring(i, i + 2));
            if (i < hex.length() - 2) {
                fps.append(":");
            }
        }
        return fps.toString();
    }
