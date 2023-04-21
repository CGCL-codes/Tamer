    protected void mergeEntries(int pos1, int pos2) {
        assert (this.numFreeEntries() == 0);
        assert (pos1 < pos2);
        this.entries[pos1].mergeWith(this.entries[pos2]);
        for (int i = pos2; i < entries.length - 1; i++) {
            entries[i] = entries[i + 1];
        }
        entries[entries.length - 1].clear();
    }
