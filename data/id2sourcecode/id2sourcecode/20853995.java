    protected void loadInfo(AudioFileFormat aff) throws UnsupportedAudioFileException {
        String type = aff.getType().toString();
        if (!type.equalsIgnoreCase("Monkey's Audio (ape)") && !type.equalsIgnoreCase("Monkey's Audio (mac)")) throw new UnsupportedAudioFileException("Not APE audio format");
        if (aff instanceof TAudioFileFormat) {
            Map props = ((TAudioFileFormat) aff).properties();
            if (props.containsKey("duration")) duration = ((Long) props.get("duration")).longValue();
            if (props.containsKey("author")) author = (String) props.get("author");
            if (props.containsKey("title")) title = (String) props.get("title");
            if (props.containsKey("copyright")) copyright = (String) props.get("copyright");
            if (props.containsKey("date")) date = (Date) props.get("date");
            if (props.containsKey("comment")) comment = (String) props.get("comment");
            if (props.containsKey("album")) album = (String) props.get("album");
            if (props.containsKey("track")) track = (String) props.get("track");
            if (props.containsKey("genre")) genre = (String) props.get("genre");
            AudioFormat af = aff.getFormat();
            channels = af.getChannels();
            samplerate = (int) af.getSampleRate();
            bitspersample = af.getSampleSizeInBits();
            if (af instanceof TAudioFormat) {
                props = ((TAudioFormat) af).properties();
                if (props.containsKey("bitrate")) bitrate = ((Integer) props.get("bitrate")).intValue();
                if (props.containsKey("ape.version")) version = ((Integer) props.get("ape.version")).intValue();
                if (props.containsKey("ape.compressionlevel")) {
                    int cl = ((Integer) props.get("ape.compressionlevel")).intValue();
                    switch(cl) {
                        case 1000:
                            compressionlevel = "Fast";
                            break;
                        case 2000:
                            compressionlevel = "Normal";
                            break;
                        case 3000:
                            compressionlevel = "High";
                            break;
                        case 4000:
                            compressionlevel = "Extra High";
                            break;
                        case 5000:
                            compressionlevel = "Insane";
                            break;
                    }
                }
                if (props.containsKey("ape.totalframes")) totalframes = ((Integer) props.get("ape.totalframes")).intValue();
                if (props.containsKey("ape.blocksperframe")) totalframes = ((Integer) props.get("ape.blocksperframe")).intValue();
                if (props.containsKey("ape.finalframeblocks")) finalframeblocks = ((Integer) props.get("ape.finalframeblocks")).intValue();
                if (props.containsKey("ape.totalblocks")) totalblocks = ((Integer) props.get("ape.totalblocks")).intValue();
                if (props.containsKey("ape.peaklevel")) peaklevel = ((Integer) props.get("ape.peaklevel")).intValue();
            }
        }
    }
