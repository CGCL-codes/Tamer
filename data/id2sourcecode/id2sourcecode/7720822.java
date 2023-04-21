    public static char[] toHexDigest(String algorithm, char[]... dt) throws NoSuchAlgorithmException {
        char[] out;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        StringBuilder sb = new StringBuilder();
        for (char[] dtp : dt) {
            sb.append(dtp);
        }
        String s = sb.toString();
        sb.delete(0, sb.length());
        byte[] bytes = s.getBytes();
        s = null;
        try {
            byte[] md5bytes = md.digest(bytes);
            out = toHex(md5bytes);
        } finally {
            int l = bytes.length;
            for (int i = 0; i < l; i++) {
                bytes[i] = 0;
            }
        }
        return out;
    }
