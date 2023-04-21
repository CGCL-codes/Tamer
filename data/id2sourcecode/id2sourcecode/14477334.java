    public List getAvailableFormats() {
        List list = new ArrayList();
        AudioFormat srcFormat = MidiToAudioSettings.DEFAULT_FORMAT;
        AudioFormat.Encoding[] encodings = AudioSystem.getTargetEncodings(srcFormat);
        for (int i = 0; i < encodings.length; i++) {
            AudioFormat dstFormat = new AudioFormat(encodings[i], srcFormat.getSampleRate(), srcFormat.getSampleSizeInBits(), srcFormat.getChannels(), srcFormat.getFrameSize(), srcFormat.getFrameRate(), srcFormat.isBigEndian());
            AudioInputStream dstStream = new AudioInputStream(null, dstFormat, 0);
            AudioFileFormat.Type[] dstTypes = AudioSystem.getAudioFileTypes(dstStream);
            if (dstTypes.length > 0) {
                list.add(new MidiToAudioFormat(dstFormat, dstTypes));
            }
        }
        return list;
    }
