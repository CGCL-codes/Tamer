    protected void execute(final Fixture fixture, final ChannelChangeProcessor processor, final String attributeName, final int dmxValue) {
        int number = fixture.getChannelNumber(attributeName);
        if (number > 0) {
            execute(processor, number, dmxValue);
        }
    }
