    public void removeMapping(int pos) {
        for (int i = pos; i < mappingCount - 1; i++) {
            map[i] = map[i + 1];
        }
        map[mappingCount - 1] = null;
        mappingCount--;
    }
