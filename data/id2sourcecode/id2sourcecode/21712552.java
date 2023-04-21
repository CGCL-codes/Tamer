    public String[][] getChannelScheduleTime(String channelPath) throws Exception {
        readFile();
        String[][] aReturn = (String[][]) htChanTimes.get(channelPath);
        return aReturn;
    }
