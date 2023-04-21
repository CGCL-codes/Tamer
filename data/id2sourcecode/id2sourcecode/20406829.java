    public int getChannelCount() {
        int count = 1;
        String countStr = deviceType.getProperty("channels");
        if (countStr != null) {
            count = Integer.parseInt(countStr);
        }
        return count;
    }
