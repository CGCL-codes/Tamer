    private void pushTogether(int hole) {
        for (int i = hole; i < (nKeys - 1); i++) {
            keys[i] = keys[i + 1];
            vals[i] = vals[i + 1];
        }
        nKeys--;
    }
