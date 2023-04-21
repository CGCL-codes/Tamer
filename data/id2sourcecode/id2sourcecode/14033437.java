    private int partitionx(int l, int r) {
        double compare = points[r].x;
        Point temp;
        int i = l - 1;
        for (int j = l; j < r; j++) {
            if (points[j].x <= compare) {
                i = i + 1;
                temp = points[i];
                points[i] = points[j];
                points[j] = temp;
            }
        }
        temp = points[r];
        points[r] = points[i + 1];
        points[i + 1] = temp;
        return i + 1;
    }
