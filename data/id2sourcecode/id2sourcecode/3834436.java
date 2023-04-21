    protected double detOfSubmatrix(int[] cols) {
        if (cols.length <= 1) {
            return data[0][cols[0]];
        } else if (cols.length == 2) {
            return data[0][cols[0]] * data[1][cols[1]] - data[1][cols[0]] * data[0][cols[1]];
        } else {
            int[] submatrixCols = new int[cols.length - 1];
            for (int i = 0; i < submatrixCols.length; i++) {
                submatrixCols[i] = cols[i + 1];
            }
            double result = 0.0;
            boolean add = (cols.length % 2) != 0;
            for (int c = 0; c < cols.length; c++) {
                if (c > 0) {
                    submatrixCols[c - 1] = cols[c - 1];
                }
                if (data[cols.length - 1][cols[c]] != 0) {
                    if (add) {
                        result += data[cols.length - 1][cols[c]] * detOfSubmatrix(submatrixCols);
                    } else {
                        result -= data[cols.length - 1][cols[c]] * detOfSubmatrix(submatrixCols);
                    }
                }
                add = !add;
            }
            return result;
        }
    }
