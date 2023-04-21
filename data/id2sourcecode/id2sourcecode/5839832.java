    @Override
    public boolean analyseFile(String thisFile) {
        boolean myReturnVal = false;
        try {
            AudioFile myAudioFile = AudioFileIO.read(new File(thisFile));
            this.put(AudioFileAttribs.AUDIO_BITRATE, myAudioFile.getBitrate());
            this.put(AudioFileAttribs.AUDIO_LENGTH_MS, new Integer(Math.round(myAudioFile.getPreciseLength() * 1000)));
            this.put(AudioFileAttribs.AUDIO_SAMPLING_RATE, myAudioFile.getSamplingRate());
            this.put(AudioFileAttribs.AUDIO_CHANNELS, myAudioFile.getChannelNumber());
            this.put(AudioFileAttribs.AUDIO_IS_VBR, myAudioFile.isVbr());
            myReturnVal = true;
        } catch (CannotReadException ex) {
            GlobalLog.logError(ex);
        }
        return myReturnVal;
    }
