    @SuppressWarnings("unchecked")
    public Map<String, Object> readSongInfo(String path) {
        Map<String, Object> properties = new HashMap<String, Object>();
        try {
            AudioFileFormat audioFileFormat = null;
            try {
                audioFileFormat = AudioSystem.getAudioFileFormat(new File(path));
            } catch (FileNotFoundException fe) {
                try {
                    audioFileFormat = AudioSystem.getAudioFileFormat(new File(getContainingFolder() + path));
                } catch (FileNotFoundException fef) {
                    return properties;
                }
            }
            AudioFormat audioFormat = audioFileFormat.getFormat();
            if (audioFileFormat instanceof TAudioFileFormat) {
                properties.putAll(((TAudioFileFormat) audioFileFormat).properties());
            }
            if (audioFileFormat.getByteLength() > 0) properties.put("audio.length.bytes", new Integer(audioFileFormat.getByteLength()));
            if (audioFileFormat.getFrameLength() > 0) properties.put("audio.length.frames", new Integer(audioFileFormat.getFrameLength()));
            if (audioFileFormat.getType() != null) properties.put("audio.type", audioFileFormat.getType().toString());
            if (audioFormat.getFrameRate() > 0) properties.put("audio.framerate.fps", new Float(audioFormat.getFrameRate()));
            if (audioFormat.getFrameSize() > 0) properties.put("audio.framesize.bytes", new Integer(audioFormat.getFrameSize()));
            if (audioFormat.getSampleRate() > 0) properties.put("audio.samplerate.hz", new Float(audioFormat.getSampleRate()));
            if (audioFormat.getSampleSizeInBits() > 0) properties.put("audio.samplesize.bits", new Integer(audioFormat.getSampleSizeInBits()));
            if (audioFormat.getChannels() > 0) properties.put("audio.channels", new Integer(audioFormat.getChannels()));
            if (audioFormat instanceof TAudioFormat) {
                Map addproperties = ((TAudioFormat) audioFormat).properties();
                properties.putAll(addproperties);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object k : properties.keySet()) {
            properties.put(k.toString(), properties.get(k).toString());
        }
        return properties;
    }
