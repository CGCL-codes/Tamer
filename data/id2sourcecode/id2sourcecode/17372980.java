    protected int getEntry(String entryname, AbstractEntry[] entries) {
        if (entries == null || entries.length == 0) return -1;
        int start = 0;
        int end = entries.length;
        while (end - start > 1 && !entries[(start + end) / 2].key.equals(entryname)) {
            int c = entries[(start + end) / 2].key.compareTo(entryname);
            if (c < 0) start = (start + end) / 2; else end = (start + end) / 2;
        }
        if (entries[(start + end) / 2].key.equals(entryname)) return (start + end) / 2; else return -1;
    }
