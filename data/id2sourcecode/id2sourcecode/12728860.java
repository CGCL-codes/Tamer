    public void nextBytes(byte[] b, int off, int len) {
        synchronized (md) {
            int i = 0;
            while (true) {
                if (bytesAvailable == 0) {
                    md.update(seed, 0, seed.length);
                    try {
                        md.digest(randomBytes, 0, randomBytes.length);
                    } catch (DigestException de) {
                    }
                    updateSeed();
                    bytesAvailable = randomBytes.length;
                }
                while (bytesAvailable > 0) {
                    if (i == len) return;
                    b[off + i] = randomBytes[--bytesAvailable];
                    i++;
                }
            }
        }
    }
