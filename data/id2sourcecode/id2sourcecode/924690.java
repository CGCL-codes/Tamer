    void removePoint(int index) {
        int npoints = x.length;
        int[] temp_x = new int[npoints - 1];
        int[] temp_y = new int[npoints - 1];
        for (int i = 0; i < index; i++) {
            temp_x[i] = x[i];
            temp_y[i] = y[i];
        }
        for (int j = index; j < npoints - 1; j++) {
            temp_x[j] = x[j + 1];
            temp_y[j] = y[j + 1];
        }
        x = temp_x;
        y = temp_y;
    }
