    public synchronized void engineNextBytes(byte[] result) {
        int index = 0;
        int todo;
        byte[] output = remainder;
        if (state == null) {
            if (seeder == null) {
                seeder = new SecureRandom(SeedGenerator.getSystemEntropy());
                seeder.engineSetSeed(engineGenerateSeed(DIGEST_SIZE));
            }
            byte[] seed = new byte[DIGEST_SIZE];
            seeder.engineNextBytes(seed);
            state = digest.digest(seed);
        }
        int r = remCount;
        if (r > 0) {
            todo = (result.length - index) < (DIGEST_SIZE - r) ? (result.length - index) : (DIGEST_SIZE - r);
            for (int i = 0; i < todo; i++) {
                result[i] = output[r];
                output[r++] = 0;
            }
            remCount += todo;
            index += todo;
        }
        while (index < result.length) {
            digest.update(state);
            output = digest.digest();
            updateState(state, output);
            todo = (result.length - index) > DIGEST_SIZE ? DIGEST_SIZE : result.length - index;
            for (int i = 0; i < todo; i++) {
                result[index++] = output[i];
                output[i] = 0;
            }
            remCount += todo;
        }
        remainder = output;
        remCount %= DIGEST_SIZE;
    }
