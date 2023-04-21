    private synchronized void initBandwidthByteChannel() throws IOException {
        if (bandwidthByteChannel == null) {
            bandwidthByteChannel = new BandwidthByteChannel(socket.getChannel(), bandwidthController);
        }
    }
