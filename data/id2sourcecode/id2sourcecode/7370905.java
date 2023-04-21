    public RDBMSDatabaseEntry get(String key) {
        if (_isSorted) {
            int low = 0;
            int high = _entryList.size() - 1;
            while (high >= low) {
                int middle = (low + high) / 2;
                RDBMSDatabaseEntry entry = _entryList.get(middle);
                int comp = entry.label().compareTo(key);
                if (comp == 0) {
                    return entry;
                }
                if (comp < 0) {
                    low = middle + 1;
                }
                if (comp > 0) {
                    high = middle - 1;
                }
            }
        } else {
            for (RDBMSDatabaseEntry entry : _entryList) {
                if (entry.label().equals(key)) {
                    return entry;
                }
            }
        }
        return null;
    }
