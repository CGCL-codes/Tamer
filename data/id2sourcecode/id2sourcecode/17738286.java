    protected void initAudioInputStream() throws PlayerException {
        try {
            if (audioSource instanceof File) {
                initAudioInputStream((File) audioSource);
            } else if (audioSource instanceof URL) {
                initAudioInputStream((URL) audioSource);
            }
            logger.info("Data source: {}", audioSource);
            if (audioFileFormat instanceof TAudioFileFormat) {
                properties = ((TAudioFileFormat) audioFileFormat).properties();
                properties = deepCopy(properties);
            } else {
                properties = new HashMap();
            }
            if (audioFileFormat.getByteLength() > 0) {
                properties.put("audio.length.bytes", new Integer(audioFileFormat.getByteLength()));
            }
            if (audioFileFormat.getFrameLength() > 0) {
                properties.put("audio.length.frames", new Integer(audioFileFormat.getFrameLength()));
            }
            if (audioFileFormat.getType() != null) {
                properties.put("audio.type", audioFileFormat.getType().toString());
            }
            AudioFormat audioFormat = audioFileFormat.getFormat();
            if (audioFormat.getFrameRate() > 0) {
                properties.put("audio.framerate.fps", new Float(audioFormat.getFrameRate()));
            }
            if (audioFormat.getFrameSize() > 0) {
                properties.put("audio.framesize.bytes", new Integer(audioFormat.getFrameSize()));
            }
            if (audioFormat.getSampleRate() > 0) {
                properties.put("audio.samplerate.hz", new Float(audioFormat.getSampleRate()));
            }
            if (audioFormat.getSampleSizeInBits() > 0) {
                properties.put("audio.samplesize.bits", new Integer(audioFormat.getSampleSizeInBits()));
            }
            if (audioFormat.getChannels() > 0) {
                properties.put("audio.channels", new Integer(audioFormat.getChannels()));
            }
            if (audioFormat instanceof TAudioFormat) {
                Map addproperties = ((TAudioFormat) audioFormat).properties();
                properties.putAll(addproperties);
            }
        } catch (UnsupportedAudioFileException ex) {
            throw new PlayerException(ex);
        } catch (IOException ex) {
            throw new PlayerException(ex);
        }
    }
