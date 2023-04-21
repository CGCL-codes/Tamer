    public int getChannelCount() {
        if (headers.containsKey("CHAN_COUNT")) return (Integer.parseInt(headers.getProperty("CHAN_COUNT")));
        return 0;
    }
