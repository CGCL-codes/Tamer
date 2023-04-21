    public AudioBuffer(final IAudioInputStream sourceStream) {
        LOGGER.log(Level.FINE, "create AudioBuffer");
        _al = PlayerMixer.get().getAl();
        _sourceStream = sourceStream;
        _al.alGenBuffers(_buffers.length, _buffers, 0);
        AlUtil.checkError(_al);
        _al.alGenSources(1, _intBuffer, 0);
        AlUtil.checkError(_al);
        _source = _intBuffer[0];
        if ((sourceStream.getSampleSize() == 1) && (sourceStream.getChannels() == 1)) {
            _format = AL.AL_FORMAT_MONO8;
            LOGGER.log(Level.FINE, "Mono 8");
        } else if ((sourceStream.getSampleSize() == 1) && (sourceStream.getChannels() == 2)) {
            _format = AL.AL_FORMAT_STEREO8;
            LOGGER.log(Level.FINE, "Stereo 8");
        } else if ((sourceStream.getSampleSize() == 2) && (sourceStream.getChannels() == 1)) {
            _format = AL.AL_FORMAT_MONO16;
            LOGGER.log(Level.FINE, "Mono 16");
        } else if ((sourceStream.getSampleSize() == 2) && (sourceStream.getChannels() == 2)) {
            _format = AL.AL_FORMAT_STEREO16;
            LOGGER.log(Level.FINE, "Stereo 16");
        } else {
            throw new IllegalArgumentException("Invalid source stream format");
        }
        _pcmBuffer = new byte[_sourceStream.getSampleRate() * _sourceStream.getFrameSize()];
        LOGGER.log(Level.FINE, "AudioBuffer created");
    }
