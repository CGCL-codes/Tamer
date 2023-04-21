    public File getChannelsConf() throws IOException {
        String channelsLocation = getString("channels..conf", "/tmp/channels.conf");
        return this.validateFile(channelsLocation);
    }
