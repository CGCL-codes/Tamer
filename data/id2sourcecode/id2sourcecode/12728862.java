    private void updateSeed() {
        long t = System.currentTimeMillis();
        byte[] tmp = new byte[8];
        for (int i = 0; i < 8; i++) {
            tmp[i] = (byte) (t & 0xff);
            t = (t >>> 8);
        }
        md.update(seed, 0, seed.length);
        md.update(tmp, 0, tmp.length);
        try {
            md.digest(seed, 0, seed.length);
        } catch (DigestException de) {
        }
    }
