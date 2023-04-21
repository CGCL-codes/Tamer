    public static byte[] createSecretKeyBytes() {
        MessageDigest digest = messageDigestThreadLocal.get();
        digest.reset();
        digest.update(lastRandomBytes);
        int time = (int) System.currentTimeMillis();
        digest.update((byte) time);
        digest.update((byte) (time >> 8));
        digest.update((byte) (time >> 16));
        digest.update((byte) (time >> 24));
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        digest.update((byte) rand.nextInt());
        byte[] key = digest.digest();
        lastRandomBytes = key;
        return key;
    }
