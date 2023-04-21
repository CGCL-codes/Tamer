    public static byte[] stretchPassphrase(byte[] passphrase, byte[] salt, int iter) {
        final SHA256Pws hasher = new SHA256Pws();
        final byte[] p = mergeBytes(passphrase, salt);
        byte[] hash = hasher.digest(p);
        for (int i = 0; i < iter; i++) {
            hash = hasher.digest(hash);
        }
        return hash;
    }
