    protected static ZCatObject createFrom(TempFile fi) {
        FSAudioFile af;
        AudioFile entagF;
        Tag tag;
        TagField tagField;
        String s;
        try {
            entagF = AudioFileIO.read(new File(fi.file.getName()));
            tag = entagF.getTag();
        } catch (CannotReadException e) {
            return new ZCatFile(fi);
        }
        af = new FSAudioFile();
        af.setFile(fi.file);
        af.setBitRate(entagF.getBitrate());
        af.setLength(entagF.getLength());
        af.setSampleRate(entagF.getSamplingRate());
        switch(entagF.getChannelNumber()) {
            case 1:
                af.setChMode(AudioChannelMode.MONO);
                break;
            case 2:
                af.setChMode(AudioChannelMode.STEREO);
                break;
            default:
                af.setChMode(AudioChannelMode.UNKNOWN);
                break;
        }
        af.setMime(s = fi.type.getBaseType());
        if (s.compareTo("audio/mpeg") == 0) getMPEGInfo(af, tag);
        return new ZCatAudioFile(af);
    }
