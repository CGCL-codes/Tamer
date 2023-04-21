    void deleteColumn(int column) {
        if ((column >= columns) || (column < 0)) {
            throw new IndexOutOfBoundsException("getCell at illegal index : " + column);
        }
        columns--;
        boolean newReserved[] = new boolean[columns];
        Object newCells[] = new Cell[columns];
        for (int i = 0; i < column; i++) {
            newReserved[i] = reserved[i];
            newCells[i] = cells[i];
            if (newCells[i] != null && (i + ((Cell) newCells[i]).getColspan() > column)) {
                ((Cell) newCells[i]).setColspan(((Cell) cells[i]).getColspan() - 1);
            }
        }
        for (int i = column; i < columns; i++) {
            newReserved[i] = reserved[i + 1];
            newCells[i] = cells[i + 1];
        }
        if (cells[column] != null && ((Cell) cells[column]).getColspan() > 1) {
            newCells[column] = cells[column];
            ((Cell) newCells[column]).setColspan(((Cell) newCells[column]).getColspan() - 1);
        }
        reserved = newReserved;
        cells = newCells;
    }
