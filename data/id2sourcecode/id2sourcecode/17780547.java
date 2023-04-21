    public int processAudio(AudioBuffer buffer) {
        ChannelFormat fmt = buffer.getChannelFormat();
        if (format != fmt) {
            format = fmt;
            controls.setFormat(format);
        }
        return AUDIO_OK;
    }
