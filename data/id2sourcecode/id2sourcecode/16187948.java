    public String nextSecureHexString(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        SecureRandom secRan = getSecRan();
        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        alg.reset();
        int numIter = (len / 40) + 1;
        StringBuffer outBuffer = new StringBuffer();
        for (int iter = 1; iter < numIter + 1; iter++) {
            byte[] randomBytes = new byte[40];
            secRan.nextBytes(randomBytes);
            alg.update(randomBytes);
            byte hash[] = alg.digest();
            for (int i = 0; i < hash.length; i++) {
                Integer c = Integer.valueOf(hash[i]);
                String hex = Integer.toHexString(c.intValue() + 128);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                outBuffer.append(hex);
            }
        }
        return outBuffer.toString().substring(0, len);
    }
