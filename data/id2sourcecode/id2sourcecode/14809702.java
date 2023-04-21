    public int getRowCount() {
        int k = 0;
        for (int i = 0; i < storage.getGroupsSize(); i++) {
            for (int j = 0; j < storage.getChannelsSize(i); j++) {
                DataChannel channel = storage.getChannel(i, j);
                k = Math.max(k, channel.size());
            }
        }
        return k;
    }
