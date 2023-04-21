    public static final int Digest(java.security.MessageDigest md, byte[] into, int into_ofs, int into_len) {
        try {
            return md.digest(into, into_ofs, into_len);
        } catch (Exception exc) {
            byte[] from = md.digest();
            int count = Math.min(from.length, into_len);
            System.arraycopy(from, 0, into, 0, count);
            return count;
        }
    }
