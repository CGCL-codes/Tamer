    private int partitiony(Point[] pty, int l, int r) {
        double compare = pty[r].y;
        Point temp;
        int i = l - 1;
        for (int j = l; j < r; j++) {
            if (pty[j].y <= compare) {
                i = i + 1;
                temp = pty[i];
                pty[i] = pty[j];
                pty[j] = temp;
            }
        }
        temp = pty[r];
        pty[r] = pty[i + 1];
        pty[i + 1] = temp;
        return i + 1;
    }
