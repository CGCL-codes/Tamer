    private AudioQuality getAudioQuality(Track track, AudioSampleEntry ase) {
        if (getFormat(ase).equals("mp4a")) {
            AudioQuality l = new AudioQuality();
            l.bitrate = getBitrate(track);
            l.audioTag = 255;
            l.samplingRate = ase.getSampleRate();
            l.channels = ase.getChannelCount();
            l.bitPerSample = ase.getSampleSize();
            l.packetSize = 4;
            l.codecPrivateData = getAudioCodecPrivateData(ase.getBoxes(ESDescriptorBox.class).get(0));
            return l;
        } else {
            throw new InternalError("I don't know what to do with audio of type " + getFormat(ase));
        }
    }
