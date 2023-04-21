    private void updateChannelNumbers() {
        for (Attribute attribute : attributes) {
            for (int i = 0; i < attribute.getChannelCount(); i++) {
                FixtureChannel channel = attribute.getChannel(i);
                int channelNumber = 0;
                if (address != 0) {
                    channelNumber = channel.getOffset() + address - 1;
                }
                channel.setNumber(channelNumber);
            }
        }
    }
