    public int getSourceLine(int pos) {
        int l = 0, r = line_number_table_length - 1;
        if (r < 0) return -1;
        int min_index = -1, min = -1;
        do {
            int i = (l + r) / 2;
            int j = line_number_table[i].getStartPC();
            if (j == pos) return line_number_table[i].getLineNumber(); else if (pos < j) r = i - 1; else l = i + 1;
            if (j < pos && j > min) {
                min = j;
                min_index = i;
            }
        } while (l <= r);
        if (min_index < 0) return -1;
        return line_number_table[min_index].getLineNumber();
    }
