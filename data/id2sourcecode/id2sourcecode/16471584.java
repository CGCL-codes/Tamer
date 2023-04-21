    public RtpConnectionImpl(String id, Connections connections) throws Exception {
        super(id, connections);
        this.isAudioCapabale = connections.endpoint.getSource(MediaType.AUDIO) != null || connections.endpoint.getSink(MediaType.AUDIO) != null;
        this.isVideoCapable = connections.endpoint.getSource(MediaType.VIDEO) != null || connections.endpoint.getSink(MediaType.VIDEO) != null;
        rtpAudioChannel = connections.rtpManager.getChannel();
        rtpVideoChannel = connections.rtpManager.getChannel();
    }
