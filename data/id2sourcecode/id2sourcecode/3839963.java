    @Override
    protected InputStream[] getChannelsInputStreams() throws IOException {
        ClassLoader classloader = this.getClass().getClassLoader();
        InputStream[] ret = new InputStream[channelsConfigLocations.length];
        int i = 0;
        for (String channelConfigLocation : channelsConfigLocations) {
            ret[i] = classloader.getResourceAsStream(channelConfigLocation);
            i++;
        }
        return ret;
    }
