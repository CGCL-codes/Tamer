    protected boolean stream(int buffer) {
        if (isStopped) return false;
        try {
            dataBuffer.clear();
            int bytesRead = getStream().read(dataBuffer, 0, dataBuffer.capacity());
            if (bytesRead >= 0) {
                dataBuffer.rewind();
                dataBuffer.limit(bytesRead);
                int format = AL10.AL_FORMAT_STEREO8;
                boolean mono = getStream().getChannelCount() == 1;
                if (getStream().getDepth() == 8) {
                    format = (mono ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_STEREO8);
                } else if (getStream().getDepth() == 16) {
                    format = (mono ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16);
                } else return false;
                AL10.alBufferData(buffer, format, dataBuffer, getStream().getBitRate());
                return true;
            }
            if (isLoop() && getTrack().isEnabled()) {
                setStream(getStream().makeNew());
                return stream(buffer);
            }
        } catch (IOException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "stream(int buffer)", "Exception", e);
        }
        return false;
    }
