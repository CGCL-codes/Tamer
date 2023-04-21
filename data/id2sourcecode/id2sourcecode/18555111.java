    public void orderColumns(int from, int to) {
        if (debug) System.out.println("Move column from: " + from + " to: " + to);
        if (from < to) for (int j = from; j < to; j++) {
            int temp;
            temp = columns[j];
            columns[j] = columns[j + 1];
            columns[j + 1] = temp;
            if (debug) System.out.println("Index: " + j);
        } else for (int j = from; j > to; j--) {
            int temp;
            temp = columns[j];
            columns[j] = columns[j - 1];
            columns[j - 1] = temp;
            if (debug) System.out.println("Index: " + j);
        }
        if (debug) if (from < to) for (int i = from; i <= to; i++) System.out.println("Index at: " + i + "=" + columns[i]); else for (int i = to; i <= from; i++) System.out.println("Index at: " + i + "=" + columns[i]);
    }
