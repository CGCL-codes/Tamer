    public AudioItem getTag(File file, String mimeType) throws TaggingException {
        AudioItemImp audioItem = new AudioItemImp();
        AudioFile audiofile;
        try {
            audiofile = AudioFileIO.read(file);
            entagged.audioformats.Tag entaggedtag = audiofile.getTag();
            audioItem.setAlbumName(entaggedtag.getFirstAlbum());
            audioItem.setArtistName(entaggedtag.getFirstArtist());
            audioItem.setName(entaggedtag.getFirstTitle());
            audioItem.setGenreName(entaggedtag.getFirstGenre());
            try {
                audioItem.setYear(Integer.parseInt(entaggedtag.getFirstYear()));
            } catch (Exception e) {
            }
            AudioInformationImp audioInfo = new AudioInformationImp();
            audioInfo.setSampleRate((long) audiofile.getSamplingRate());
            audioInfo.setBitrate((long) audiofile.getBitrate());
            audioInfo.setNumberAudioChannels(audiofile.getChannelNumber());
            AudioResourceImp audioResource = new AudioResourceImp();
            audioResource.setLocation(file.toURI().toString());
            audioResource.setMimeType(mimeType);
            audioItem.setDuration((long) (audiofile.getPreciseLength() * 1000.0));
            audioResource.setDuration(audioItem.getDuration());
            audioResource.setAudioInformation(audioInfo);
            audioItem.setFirstResource(audioResource);
            try {
                String trackString = entaggedtag.getFirstTrack();
                if (trackString != null && trackString.length() > 0) {
                    String[] trackNumbers = trackString.split("/");
                    if (trackNumbers.length > 0) {
                        audioItem.setAlbumOrder(Integer.parseInt(trackNumbers[0]));
                    }
                }
            } catch (NumberFormatException e) {
                logger.error("Error parsing number", e);
                logger.error("Track number is:{}", entaggedtag.getFirstTrack());
                logger.info("No Tracknumber for file " + file.toString());
            }
            if (audioItem.getName() == null || audioItem.getName().length() < 1) {
                audioItem.setName(file.getName());
            }
            return audioItem;
        } catch (CannotReadException e) {
            logger.error("Can't Read:" + file.toString() + " " + e.toString());
            throw new TaggingException("Error getting tag:", e);
        } catch (Exception e) {
            logger.error("Exception for file:" + file.toString() + " " + e.toString());
            throw new TaggingException("Error getting tag:", e);
        }
    }
