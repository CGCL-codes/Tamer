    void removeBall(int index) {
        int npoints = all_x.length;
        int[] temp_all_x = new int[npoints - 1];
        int[] temp_all_y = new int[npoints - 1];
        double[] temp_all_radii = new double[npoints - 1];
        for (int i = 0; i < index; i++) {
            temp_all_x[i] = all_x[i];
            temp_all_y[i] = all_y[i];
            temp_all_radii[i] = all_radii[i];
        }
        for (int j = index; j < npoints - 1; j++) {
            temp_all_x[j] = all_x[j + 1];
            temp_all_y[j] = all_y[j + 1];
            temp_all_radii[j] = all_radii[j + 1];
        }
        all_x = temp_all_x;
        all_y = temp_all_y;
        all_radii = temp_all_radii;
    }
