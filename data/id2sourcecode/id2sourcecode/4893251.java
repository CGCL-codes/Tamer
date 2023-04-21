    public static byte getNonZeroRandomByte(MessageDigest md5_) {
        String randomString = String.valueOf(System.currentTimeMillis() * Math.random());
        md5_.reset();
        byte[] randomBytes = md5_.digest(randomString.getBytes());
        for (int i = 0; i < 20; i++) {
            byte b = 0;
            if (i < randomBytes.length) b = randomBytes[i];
            if (b != 0) return b;
        }
        return getNonZeroRandomByte(md5_);
    }
