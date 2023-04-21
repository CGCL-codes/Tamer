    protected byte[] hash(byte[] bytes, byte[] salt, int hashIterations) {
        MessageDigest md = getDigest(getAlgorithmName());
        if (salt != null) {
            md.reset();
            md.update(salt);
        }
        byte[] hashed = md.digest(bytes);
        int iterations = hashIterations - 1;
        for (int i = 0; i < iterations; i++) {
            md.reset();
            hashed = md.digest(hashed);
        }
        return hashed;
    }
