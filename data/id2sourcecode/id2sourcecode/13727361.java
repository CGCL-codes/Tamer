    private static void generateHashkey(byte[] id, int[] hash_key) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("md5");
            sha.update(id);
            byte[] out = sha.digest();
            int offset = 0;
            for (int i = 0; i < 4; i++) {
                int temp = ByteArrayUtility.toInteger(out, offset);
                offset += 4;
                if (temp < 0) {
                    temp = (-1) * temp + 2 ^ 31;
                }
                hash_key[i] = temp % (DIGEST_SIZE * 32);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new HyperCastFatalRuntimeException(e);
        }
    }
