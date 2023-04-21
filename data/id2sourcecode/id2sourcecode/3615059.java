    public static String toHexDigest(MessageDigest digest) {
        if (digest == null) {
            return null;
        }
        byte[] result = digest.digest();
        StringBuffer hexDigest = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            SVNFormatUtil.appendHexNumber(hexDigest, result[i]);
        }
        return hexDigest.toString();
    }
