    public static String getSHAEncoded(String text) throws Exception {
        if (text == null || text.length() < 1) return "";
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] intermediateHash = md.digest(text.getBytes());
        md.update(intermediateHash);
        return new String(ByteUtils.getCharsFromBCD(md.digest(salt), false, false));
    }
