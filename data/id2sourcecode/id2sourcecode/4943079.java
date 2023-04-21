    public static long[] hash(String str) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }
        byte[] md5bytes = digest.digest(str.getBytes());
        long[] res = new long[2];
        res[0] = res[1] = 0;
        BigInteger md5Int = new BigInteger(1, md5bytes);
        res[0] = md5Int.shiftRight(64).longValue();
        res[1] = md5Int.and(MASK).longValue();
        return res;
    }
