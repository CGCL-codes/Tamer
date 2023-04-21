    static synchronized void computePRF_SSLv3(byte[] out, byte[] secret, byte[] seed) {
        if (sha == null) {
            init();
        }
        int pos = 0;
        int iteration = 1;
        byte[] digest;
        while (pos < out.length) {
            byte[] pref = new byte[iteration];
            Arrays.fill(pref, (byte) (64 + iteration++));
            sha.update(pref);
            sha.update(secret);
            sha.update(seed);
            md5.update(secret);
            md5.update(sha.digest());
            digest = md5.digest();
            if (pos + 16 > out.length) {
                System.arraycopy(digest, 0, out, pos, out.length - pos);
                pos = out.length;
            } else {
                System.arraycopy(digest, 0, out, pos, 16);
                pos += 16;
            }
        }
    }
