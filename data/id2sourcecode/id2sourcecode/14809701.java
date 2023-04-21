    public java.lang.String getColumnName(int column) {
        int k = 0;
        if (column == 0) return "";
        for (int i = 0; i < storage.getGroupsSize(); i++) {
            for (int j = 0; j < storage.getChannelsSize(i); j++) {
                k++;
                if (k == column) return storage.getChannel(i, j).getName();
            }
        }
        return "null";
    }
