    protected final boolean fillMetaData(AudioMeta am) {
        AudioFile entagF;
        Tag tag;
        String s;
        try {
            entagF = AudioFileIO.read(obj);
            tag = entagF.getTag();
        } catch (CannotReadException e) {
            return false;
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
        am.setMime(s = type.getBaseType());
        if (s.compareTo("audio/mpeg") == 0) {
            am.setAlbum(MiscUtils.nullString(tag.getFirstAlbum()));
            am.setArtist(MiscUtils.nullString(tag.getFirstArtist()));
            am.setGenre(MiscUtils.nullString(tag.getFirstGenre()));
            am.setTitle(MiscUtils.nullString(tag.getFirstTitle()));
            am.setTrack(MiscUtils.nullString(tag.getFirstTrack()));
            am.setYear(MiscUtils.nullString(tag.getFirstYear()));
        }
        return true;
    }
