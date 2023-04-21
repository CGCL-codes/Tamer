    private void shuffleNeighborOffsets() {
        for (int i = 0; i < 8; i++) {
            int j = i + rnd.nextInt(8 - i);
            int t = neighborOffsets[i];
            neighborOffsets[i] = neighborOffsets[j];
            neighborOffsets[j] = t;
            t = neighborXYOffsets[i][0];
            neighborXYOffsets[i][0] = neighborXYOffsets[j][0];
            neighborXYOffsets[j][0] = t;
            t = neighborXYOffsets[i][1];
            neighborXYOffsets[i][1] = neighborXYOffsets[j][1];
            neighborXYOffsets[j][1] = t;
        }
    }
