    public int getColumnCount() {
        int k = 0;
        for (int i = 0; i < storage.getGroupsSize(); i++) {
            k += storage.getChannelsSize(i);
        }
        return k + 1;
    }
