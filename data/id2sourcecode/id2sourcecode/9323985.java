    public static double[][] rank(double data[][], boolean reversed, int rowcolumn) throws IllegalArgumentException {
        if (rowcolumn != ROW && rowcolumn != COLUMN) throw new IllegalArgumentException("rowcolumn needs to be either Statistical.ROW or Statistical.COLUMN");
        int rows = data.length;
        int cols = data[0].length;
        double ranks[][] = new double[rows][cols];
        if (rowcolumn == COLUMN) {
            double col[] = new double[rows];
            for (int c = 0; c < cols; ++c) {
                for (int r = 0; r < rows; ++r) col[r] = data[r][c];
                Arrays.sort(col);
                for (int r = 0; r < rows; ++r) {
                    double val = data[r][c];
                    int low = 0;
                    int mid = rows / 2;
                    int hgh = rows - 1;
                    while (val != col[mid]) {
                        if (val < col[mid]) hgh = mid - 1; else low = mid + 1;
                        mid = (low + hgh) / 2;
                    }
                    int jt = 0;
                    int ties = 0;
                    jt = mid - 1;
                    while (jt >= 0 && col[jt--] == val) ties++;
                    jt = mid + 1;
                    if (ties != 0) mid -= ties;
                    while (jt < rows && col[jt++] == val) ties++;
                    if (ties == 0) ranks[r][c] = (reversed ? rows - mid : mid + 1); else ranks[r][c] = (reversed ? rows - (0.5 * (mid + mid + ties) + 1) : (0.5 * (mid + mid + ties) + 1));
                }
            }
        } else if (rowcolumn == ROW) {
            double row[] = new double[cols];
            for (int r = 0; r < rows; ++r) {
                System.arraycopy(data[r], 0, row, 0, cols);
                Arrays.sort(row);
                for (int c = 0; c < cols; ++c) {
                    double val = data[r][c];
                    int low = 0;
                    int mid = cols / 2;
                    int hgh = cols - 1;
                    while (val != row[mid]) {
                        if (val < row[mid]) hgh = mid - 1; else low = mid + 1;
                        mid = (low + hgh) / 2;
                    }
                    int jt = 0;
                    int ties = 0;
                    jt = mid - 1;
                    while (jt >= 0 && row[jt--] == val) ties++;
                    jt = mid + 1;
                    if (ties != 0) mid -= ties;
                    while (jt < cols && row[jt++] == val) ties++;
                    if (ties == 0) ranks[r][c] = (reversed ? cols - mid : mid + 1); else ranks[r][c] = (reversed ? cols - (0.5 * (mid + mid + ties) + 1) : (0.5 * (mid + mid + ties) + 1));
                }
            }
        }
        return ranks;
    }
