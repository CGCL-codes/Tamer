    static byte[] stretchPassphrase(byte[] passphrase, byte[] salt, int iter) {
        byte[] p = Util.mergeBytes(passphrase, salt);
        byte[] hash = SHA256Pws.digest(p);
        for (int i = 0; i < iter; i++) {
            hash = SHA256Pws.digest(hash);
        }
        return hash;
    }
