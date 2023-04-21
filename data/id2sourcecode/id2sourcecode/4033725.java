    public void insertData(String chName, Object data) {
        int chIndex = getChannelIndex(chName);
        insertData(chIndex, data);
    }
