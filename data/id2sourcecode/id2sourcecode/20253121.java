    private void generateRandomPack2(BitSet pack) {
        pack.set(0, N, true);
        pack.set(N, C, false);
        for (int i = 0; i < N; i++) {
            int j = i + random.nextInt(C - i);
            if (i != j) {
                boolean temp = pack.get(i);
                pack.set(i, pack.get(j));
                pack.set(j, temp);
            }
        }
    }
