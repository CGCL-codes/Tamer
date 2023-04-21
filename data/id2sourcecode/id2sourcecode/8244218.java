    private Channel getChannel(MediaType mediaType) {
        switch(mediaType) {
            case AUDIO:
                return this.audioChannel;
            case VIDEO:
                return this.videoChannel;
            default:
                return null;
        }
    }
