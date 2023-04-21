    public static long hash(InputStream s) {
        try {
            DigestInputStream dis = new DigestInputStream(s, MessageDigest.getInstance("MD5"));
            byte[] buf = new byte[100];
            while (dis.read(buf, 0, buf.length) != -1) ;
            byte[] hash = dis.getMessageDigest().digest();
            return ((long) (hash[hash.length - 8] & 0xff) << 56) | ((long) (hash[hash.length - 7] & 0xff) << 48) | ((long) (hash[hash.length - 6] & 0xff) << 40) | ((long) (hash[hash.length - 5] & 0xff) << 32) | ((long) (hash[hash.length - 4] & 0xff) << 24) | ((long) (hash[hash.length - 3] & 0xff) << 16) | ((long) (hash[hash.length - 2] & 0xff) << 8) | ((long) (hash[hash.length - 1] & 0xff));
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
