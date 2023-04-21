    private int getOffset(TableItem topItem) {
        int rowHeight = table.getItemHeight();
        int from = 0;
        int to = rowHeight + 1;
        int test;
        for (int i = 0; i <= rowHeight; i++) {
            test = from + (to - from) / 2;
            if (table.getItem(new Point(0, test)) == topItem) {
                if (table.getItem(new Point(0, test + 1)) != topItem) return rowHeight - test - 1; else from = test;
            } else {
                if (table.getItem(new Point(0, test - 1)) == topItem) return rowHeight - test - 1; else to = test;
            }
        }
        return 0;
    }
