    @Override
    public void load(File input) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(input);
        type = aff.getType().toString();
        if (!type.equalsIgnoreCase("ogg")) {
            throw new UnsupportedAudioFileException("Not Ogg Vorbis audio format");
        }
        size = input.length();
        location = input.getPath();
        VorbisCommentTag vctag = null;
        GenericAudioHeader gah = null;
        try {
            AudioFile oggFile = AudioFileIO.read(input);
            vctag = (VorbisCommentTag) oggFile.getTag();
            OggInfoReader oir = new OggInfoReader();
            gah = oir.read(new RandomAccessFile(input, "r"));
            if (gah != null) {
                type = gah.getEncodingType();
                channels = gah.getChannelNumber();
                samplerate = gah.getSampleRateAsNumber();
                bitrate = (int) gah.getBitRateAsNumber();
                totalms = gah.getTrackLength();
            }
            if (vctag != null) {
                vendor = vctag.getVendor();
                title = vctag.getFirstTitle();
                artist = vctag.getFirstArtist();
                album = vctag.getFirstAlbum();
                year = vctag.getFirstYear();
                genre = vctag.getFirstGenre();
                track = vctag.getFirstTrack();
                comment = vctag.getFirstComment();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
