    public void invert_position_selection(int in_col, int in_row) {
        int i, j;
        boolean found;
        found = false;
        for (i = 0; i <= levelmax; i++) {
            if (collist[i] == in_col && rowlist[i] == in_row) {
                found = true;
                break;
            }
        }
        if (found == true) {
            for (j = i; j < levelmax; j++) {
                collist[j] = collist[j + 1];
                rowlist[j] = rowlist[j + 1];
            }
            levelmax--;
        } else {
            levelmax++;
            collist[levelmax] = in_col;
            rowlist[levelmax] = in_row;
        }
    }
