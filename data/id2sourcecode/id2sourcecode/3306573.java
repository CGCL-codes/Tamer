    public void remove(int r) {
        for (int i = r; i < (size - 1); ++i) {
            modules[i] = modules[i + 1];
            for (int j = 0; j < size; ++j) {
                values[i][j] = values[i + 1][j];
            }
        }
        for (int i = 0; i < (size - 1); ++i) {
            for (int j = r; j < (size - 1); ++j) {
                values[i][j] = values[i][j + 1];
            }
        }
        size--;
    }
