    public void sortOnColumn(boolean sort_ascending, int col_idx) {
        boolean made_change = true;
        while (made_change) {
            made_change = false;
            for (int i = 0; i < data_grid.length - 1; i++) {
                boolean flip = false;
                if (data_grid[i][col_idx].equals("") || data_grid[i + 1][col_idx].equals("")) {
                    if (data_grid[i][col_idx].equals("") && data_grid[i + 1][col_idx].equals("")) {
                    } else {
                        if ((sort_ascending && !data_grid[i][col_idx].equals("") && data_grid[i + 1][col_idx].equals("")) || (!sort_ascending && data_grid[i][col_idx].equals("") && !data_grid[i + 1][col_idx].equals(""))) {
                            flip = true;
                        }
                    }
                } else {
                    if ((sort_ascending && (Float.parseFloat(data_grid[i][col_idx]) > Float.parseFloat(data_grid[i + 1][col_idx]))) || (!sort_ascending && (Float.parseFloat(data_grid[i][col_idx]) < Float.parseFloat(data_grid[i + 1][col_idx])))) {
                        flip = true;
                    } else {
                    }
                }
                if (flip) {
                    String[] temp = data_grid[i];
                    data_grid[i] = data_grid[i + 1];
                    data_grid[i + 1] = temp;
                    String[] stemp = vert[i];
                    vert[i] = vert[i + 1];
                    vert[i + 1] = stemp;
                    made_change = true;
                }
            }
        }
    }
