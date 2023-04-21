    protected void initSettings() {
        dim = grid.length;
        distance = new double[dim - 1];
        for (int i = 0; i < dim - 1; i++) distance[i] = grid[i + 1] - grid[i];
        bezier = new double[3 * dim - 2];
        a = new double[dim - 2];
        b = new double[dim - 2];
        rightSide = new double[dim - 2];
        mu = new double[dim];
        for (int j = 0; j < a.length; j++) {
            a[j] = distance[j] / (distance[j] + distance[j + 1]);
            b[j] = distance[j + 1] / (distance[j] + distance[j + 1]);
            rightSide[j] = (fvalues[j + 2] - fvalues[j + 1]) / distance[j + 1];
            rightSide[j] -= (fvalues[j + 1] - fvalues[j]) / distance[j];
            rightSide[j] /= (distance[j] + distance[j + 1]);
        }
    }
