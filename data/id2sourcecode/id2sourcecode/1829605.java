    public int getLineForOffset(int offset) {
        int line;
        int min;
        int max;
        int off1;
        int off2;
        synchronized (index) {
            if (charCount > 0 && offset >= charCount) return index.size() - 1;
            min = 0;
            max = index.size() - 1;
            if (max <= 0) return 0;
            do {
                line = min + (max - min) / 2;
                off1 = index.get(line);
                off2 = index.get(line + 1);
                if (offset >= off1 && offset < off2) max = min = line; else if (offset >= off2) min = line + 1; else max = line - 1;
            } while (max > min);
        }
        line = min + (max - min) / 2;
        return line;
    }
