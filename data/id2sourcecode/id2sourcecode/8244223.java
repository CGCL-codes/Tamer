    public CheckPoint getCheckPoint(MediaType mediaType, int n) {
        switch(n) {
            case 1:
                MediaSource s = endpoint.getSource(mediaType);
                return s == null ? new CheckPointImpl(0, 0) : new CheckPointImpl(s.getPacketsTransmitted(), s.getBytesTransmitted());
            case 2:
                MediaSink sink = endpoint.getSink(mediaType);
                return sink == null ? new CheckPointImpl(0, 0) : new CheckPointImpl(sink.getPacketsReceived(), sink.getBytesReceived());
            case 3:
                sink = this.getChannel(mediaType).splitter.getInput();
                return new CheckPointImpl(sink.getPacketsReceived(), sink.getBytesReceived());
            case 4:
                s = this.getChannel(mediaType).mixer.getOutput();
                return new CheckPointImpl(s.getPacketsTransmitted(), s.getBytesTransmitted());
            default:
                throw new IllegalArgumentException("Unknown check point");
        }
    }
