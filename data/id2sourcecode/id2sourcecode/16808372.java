    private void ensureIsSeeded() {
        if (!seeded) {
            new Random(0L).nextBytes(seed);
            byte[] digestdata = digest.digest(data);
            System.arraycopy(digestdata, 0, data, 0, SEED_SIZE);
            seeded = true;
        }
    }
