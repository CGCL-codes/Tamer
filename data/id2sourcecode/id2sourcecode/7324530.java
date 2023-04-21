    protected void setAttribute(final Fixture fixture, final String attributeName, final int dmxValue) {
        int number = fixture.getChannelNumber(attributeName);
        if (number > 0) {
            ChannelChange cc = new ChannelChange(number, dmxValue);
            ccs.add(cc);
        }
    }
