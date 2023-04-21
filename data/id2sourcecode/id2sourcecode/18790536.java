    public Object makeKey(byte[] uk, int bs) throws InvalidKeyException {
        if (bs != DEFAULT_BLOCK_SIZE) throw new IllegalArgumentException();
        if (uk == null) throw new InvalidKeyException("Empty key");
        if (uk.length != DEFAULT_KEY_SIZE) throw new InvalidKeyException("Key is not 128-bit.");
        int[][] Ke = new int[ROUNDS + 1][4];
        int[][] Kd = new int[ROUNDS + 1][4];
        int[][] tK = new int[ROUNDS + 1][4];
        int i = 0;
        Ke[0][0] = (uk[i++] & 0xFF) << 24 | (uk[i++] & 0xFF) << 16 | (uk[i++] & 0xFF) << 8 | (uk[i++] & 0xFF);
        tK[0][0] = Ke[0][0];
        Ke[0][1] = (uk[i++] & 0xFF) << 24 | (uk[i++] & 0xFF) << 16 | (uk[i++] & 0xFF) << 8 | (uk[i++] & 0xFF);
        tK[0][1] = Ke[0][1];
        Ke[0][2] = (uk[i++] & 0xFF) << 24 | (uk[i++] & 0xFF) << 16 | (uk[i++] & 0xFF) << 8 | (uk[i++] & 0xFF);
        tK[0][2] = Ke[0][2];
        Ke[0][3] = (uk[i++] & 0xFF) << 24 | (uk[i++] & 0xFF) << 16 | (uk[i++] & 0xFF) << 8 | (uk[i] & 0xFF);
        tK[0][3] = Ke[0][3];
        int j;
        for (i = 1, j = 0; i < ROUNDS + 1; i++, j++) {
            tK[i][0] = tK[j][0] ^ rot32L(tK[j][3], 8) ^ OFFSET[j];
            tK[i][1] = tK[j][1] ^ tK[i][0];
            tK[i][2] = tK[j][2] ^ tK[i][1];
            tK[i][3] = tK[j][3] ^ tK[i][2];
            System.arraycopy(tK[i], 0, Ke[i], 0, 4);
            transform(Ke[j], Ke[j]);
        }
        for (i = 0; i < ROUNDS; i++) System.arraycopy(tK[ROUNDS - i], 0, Kd[i], 0, 4);
        transform(tK[0], Kd[ROUNDS]);
        return new Object[] { Ke, Kd };
    }
