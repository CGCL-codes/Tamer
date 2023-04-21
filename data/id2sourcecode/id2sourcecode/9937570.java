    public OggAudioStream(String file) {
        String path = AppDefinition.getMusicDirectoryPath() + "/" + file;
        os = null;
        try {
            os = new FileStream(new RandomAccessFile(path, "r"));
        } catch (Exception e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't create sound stream!");
            return;
        }
        los = (LogicalOggStream) os.getLogicalStreams().iterator().next();
        if (los.getFormat() != LogicalOggStream.FORMAT_VORBIS) {
            Console.print("File is not in proper ogg vorbis format.");
            return;
        }
        vs = null;
        openStream();
        channels = vs.getIdentificationHeader().getChannels();
        sampleRate = vs.getIdentificationHeader().getSampleRate();
    }
