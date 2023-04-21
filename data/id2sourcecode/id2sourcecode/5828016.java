    private void updateN_M(int[] n_M, DiscreteEstimator de) {
        int n = n_M.length;
        int[] tmp = new int[n];
        tmp[n - 1] = (int) de.getSumOfCounts();
        n_M[n - 1] += tmp[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            tmp[i] = tmp[i + 1] - (int) de.getCount(i + 1);
            n_M[i] += tmp[i];
        }
    }
