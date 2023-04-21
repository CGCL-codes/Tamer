    public static byte[] reduceHash(MessageDigest md, int nrBytes) {
        byte[] digest = md.digest();
        byte[] res = new byte[nrBytes];
        for (int dx = 0; dx < digest.length; dx++) res[dx % nrBytes] ^= digest[dx];
        return res;
    }
