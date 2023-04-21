    private int[] push(int[] ind, int sub) {
        int[] new_ind = new int[ind.length - 1];
        if (sub == 0) {
            for (int i = 0; i < (ind.length - 1); i++) {
                new_ind[i] = ind[i + 1];
            }
        } else if (sub == ind.length) {
            for (int i = 0; i < (ind.length - 1); i++) {
                new_ind[i] = ind[i];
            }
        } else {
            for (int i = 0; i < sub; i++) {
                new_ind[i] = ind[i];
            }
            for (int i = sub; i < (ind.length - 1); i++) {
                new_ind[i] = ind[i + 1];
            }
        }
        return new_ind;
    }
