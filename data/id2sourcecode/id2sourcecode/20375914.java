    @Override
    public void load(File input) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(input);
        type = aff.getType().toString();
        if (!type.equalsIgnoreCase("mp3")) {
            throw new UnsupportedAudioFileException("Not MP3 audio format");
        }
        size = input.length();
        location = input.getPath();
        MP3AudioHeader audioHeader = null;
        Tag tag = null;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(input);
            audioHeader = mp3File.getMP3AudioHeader();
            tag = mp3File.getTag();
            if (audioHeader != null) {
                type = audioHeader.getEncodingType();
                encoder = audioHeader.getEncoder();
                channelsMode = audioHeader.getChannels();
                if (channelsMode.equals("Stereo")) {
                    channels = 2;
                } else if (channelsMode.equals("Joint Stereo")) {
                    channels = 2;
                } else if (channelsMode.equals("Dual")) {
                    channels = 2;
                } else if (channelsMode.equals("Mono")) {
                    channels = 1;
                } else {
                    channels = 0;
                }
                samplerate = audioHeader.getSampleRateAsNumber();
                bitrate = (int) audioHeader.getBitRateAsNumber();
                layer = audioHeader.getMpegLayer();
                version = audioHeader.getMpegVersion();
                crc = audioHeader.isProtected();
                vbr = audioHeader.isVariableBitRate();
                copyright = audioHeader.isCopyrighted();
                original = audioHeader.isOriginal();
                privat = audioHeader.isPrivate();
                emphasis = audioHeader.getEmphasis();
                totals = audioHeader.getTrackLength();
            }
            if (tag != null) {
                title = tag.getFirstTitle();
                artist = tag.getFirstArtist();
                album = tag.getFirstAlbum();
                year = tag.getFirstYear();
                genre = tag.getFirstGenre();
                track = tag.getFirstTrack();
                comment = tag.getFirstComment();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
