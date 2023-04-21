    private int[] doAbsorbingRandowWalk(final Matrix mP, int highest) {
        final int size = mP.numRows();
        final int[] ranking = new int[size];
        ranking[0] = highest;
        final int[] map = new int[size];
        for (int i = 0; i < highest; ++i) map[i] = i;
        for (int i = highest; i < size - 1; ++i) map[i] = i + 1;
        for (int absorbed = 1; absorbed < size; ++absorbed) {
            System.out.println("absorbed " + absorbed);
            final int sizeOfQ = size - absorbed;
            final Matrix mIminusQ = new DenseMatrix(sizeOfQ, sizeOfQ);
            for (int i = 0; i < sizeOfQ; ++i) for (int j = 0; j < sizeOfQ; ++j) mIminusQ.set(i, j, (i == j ? 1.0 : 0.0) - mP.get(map[i], map[j]));
            final Matrix mI = Matrices.identity(sizeOfQ);
            final Matrix mN = mI.copy();
            mIminusQ.solve(mI, mN);
            final Vector v = new DenseVector(sizeOfQ);
            for (int i = 0; i < sizeOfQ; ++i) {
                for (int j = 0; j < sizeOfQ; ++j) v.add(i, mN.get(i, j));
                v.set(i, v.get(i) / sizeOfQ);
            }
            highest = findHighestRanked(v);
            ranking[absorbed] = map[highest];
            for (int i = highest; i < size - 1; ++i) map[i] = map[i + 1];
        }
        return ranking;
    }
