    public int getChannelNumber(final String string) {
        int number = 0;
        FixtureChannel fc = channelMap.get(string);
        if (fc != null) {
            number = fc.getNumber();
        }
        return number;
    }
