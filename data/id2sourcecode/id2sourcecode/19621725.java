    public ConnectionConsumer(RTMPConnection conn, byte videoChannel, byte audioChannel, byte dataChannel) {
        this.conn = conn;
        this.video = conn.getChannel(videoChannel);
        this.audio = conn.getChannel(audioChannel);
        this.data = conn.getChannel(dataChannel);
        streamTracker = new StreamTracker();
    }
