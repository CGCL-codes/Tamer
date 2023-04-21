    public static String bubbleBabble(byte[] blob) {
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
            sha1.update(blob);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("SSH2KeyFingerprint.bubbleBabble: " + e);
        }
        byte[] raw = sha1.digest();
        StringBuffer retval = new StringBuffer();
        char[] consonants = { 'b', 'c', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'z', 'x' };
        char[] vowels = { 'a', 'e', 'i', 'o', 'u', 'y' };
        int rounds = (raw.length / 2) + 1;
        int seed = 1;
        retval.append('x');
        for (int i = 0; i < rounds; i++) {
            int idx0, idx1, idx2, idx3, idx4;
            if ((i + 1 < rounds) || ((raw.length % 2) != 0)) {
                idx0 = ((((((int) (raw[2 * i])) & 0xff) >>> 6) & 3) + seed) % 6;
                idx1 = ((((int) (raw[2 * i])) & 0xff) >>> 2) & 15;
                idx2 = (((((int) (raw[2 * i])) & 0xff) & 3) + (seed / 6)) % 6;
                retval.append(vowels[idx0]);
                retval.append(consonants[idx1]);
                retval.append(vowels[idx2]);
                if ((i + 1) < rounds) {
                    idx3 = ((((int) (raw[(2 * i) + 1])) & 0xff) >>> 4) & 15;
                    idx4 = (((int) (raw[(2 * i) + 1])) & 0xff) & 15;
                    retval.append(consonants[idx3]);
                    retval.append('-');
                    retval.append(consonants[idx4]);
                    seed = ((seed * 5) + (((((int) (raw[2 * i])) & 0xff) * 7) + (((int) (raw[(2 * i) + 1])) & 0xff))) % 36;
                }
            } else {
                idx0 = seed % 6;
                idx1 = 16;
                idx2 = seed / 6;
                retval.append(vowels[idx0]);
                retval.append(consonants[idx1]);
                retval.append(vowels[idx2]);
            }
        }
        retval.append('x');
        return retval.toString();
    }
