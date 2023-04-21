    public FlacAudioFormat(StreamInfo streamInfo) {
        super(FlacEncoding.FLAC, streamInfo.getSampleRate(), streamInfo.getBitsPerSample(), streamInfo.getChannels(), AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false);
        props = new HashMap();
        props.put(KEY_FRAMESIZE_MIN, new Integer(streamInfo.getMinFrameSize()));
        props.put(KEY_FRAMESIZE_MAX, new Integer(streamInfo.getMaxFrameSize()));
        props.put(KEY_BLOCKSIZE_MIN, new Integer(streamInfo.getMinBlockSize()));
        props.put(KEY_BLOCKSIZE_MAX, new Integer(streamInfo.getMaxBlockSize()));
    }
