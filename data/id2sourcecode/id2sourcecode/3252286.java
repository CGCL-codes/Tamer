    static void doAudio(FSEntry m) {
        FileMeta fm = m.getFile();
        AudioMeta am = new AudioMeta();
        AudioFile entagF;
        Tag tag;
        String s;
        try {
            entagF = AudioFileIO.read(m.getAsFile());
            tag = entagF.getTag();
        } catch (CannotReadException e) {
            return;
        }
        am.setBitRate(entagF.getBitrate());
        am.setLength(entagF.getLength());
        am.setSampleRate(entagF.getSamplingRate());
        switch(entagF.getChannelNumber()) {
            case 1:
                am.setChMode(AudioChannelMode.MONO);
                break;
            case 2:
                am.setChMode(AudioChannelMode.STEREO);
                break;
            default:
                am.setChMode(AudioChannelMode.UNKNOWN);
                break;
        }
        am.setMime(s = m.getMimeType().getBaseType());
        if (s.compareTo("audio/mpeg") == 0) {
            am.setAlbum(nulls(tag.getFirstAlbum()));
            am.setArtist(nulls(tag.getFirstArtist()));
            am.setGenre(nulls(tag.getFirstGenre()));
            am.setTitle(nulls(tag.getFirstTitle()));
            am.setTrack(nulls(tag.getFirstTrack()));
            am.setYear(nulls(tag.getFirstYear()));
        }
        copyFields(fm, am);
        m.setFile(am);
    }
