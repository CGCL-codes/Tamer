    private void initChannels() {
        String string = definition.getChannels();
        String[] substrings = string.split(",");
        for (int i = 0; i < substrings.length; i++) {
            int offset = Util.toInt(substrings[i]);
            String channelName = channelName(i);
            channels.add(new FixtureChannel(channelName, offset));
        }
    }
