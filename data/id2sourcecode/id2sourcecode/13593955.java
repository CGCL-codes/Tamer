    @Override
    public AudioFileFormat getAudioFileFormat(InputStream stream, long lFileLengthInBytes) throws UnsupportedAudioFileException, IOException {
        Map<String, Object> aff_properties = new HashMap<String, Object>();
        Map<String, Object> af_properties = new HashMap<String, Object>();
        int channels = AudioSystem.NOT_SPECIFIED;
        int bitsPerSample = AudioSystem.NOT_SPECIFIED;
        int sampleRate = AudioSystem.NOT_SPECIFIED;
        long totalSamples = AudioSystem.NOT_SPECIFIED;
        int duration = AudioSystem.NOT_SPECIFIED;
        int frameSizeMin = AudioSystem.NOT_SPECIFIED;
        int frameSizeMax = AudioSystem.NOT_SPECIFIED;
        int blockSizeMin = AudioSystem.NOT_SPECIFIED;
        int blockSizeMax = AudioSystem.NOT_SPECIFIED;
        try {
            decoder = new FLACDecoder(stream);
            streamInfo = decoder.readStreamInfo();
            if (streamInfo == null) {
                if (DEBUG) {
                    System.out.println("FLAC file reader: no stream info found");
                }
                throw new UnsupportedAudioFileException("No StreamInfo found");
            }
            bitsPerSample = streamInfo.getBitsPerSample();
            channels = streamInfo.getChannels();
            sampleRate = streamInfo.getSampleRate();
            totalSamples = streamInfo.getTotalSamples();
            duration = Math.round(totalSamples / sampleRate);
            frameSizeMin = streamInfo.getMinFrameSize();
            frameSizeMax = streamInfo.getMaxFrameSize();
            blockSizeMin = streamInfo.getMinBlockSize();
            blockSizeMax = streamInfo.getMaxBlockSize();
            aff_properties.put("flac.bitpersample", new Integer(bitsPerSample));
            aff_properties.put("flac.channels", new Integer(channels));
            aff_properties.put("flac.sampleRate", new Integer(sampleRate));
            aff_properties.put("flac.totalSamples", new Long(totalSamples));
            aff_properties.put("duration", new Long(duration * 1000000L));
            aff_properties.put("flac.framesize.min", new Integer(frameSizeMin));
            aff_properties.put("flac.framesize.max", new Integer(frameSizeMax));
            aff_properties.put("flac.blocksize.min", new Integer(blockSizeMin));
            aff_properties.put("flac.blocksize.max", new Integer(blockSizeMax));
            af_properties.put("bitrate", new Integer(AudioSystem.NOT_SPECIFIED));
            af_properties.put("vbr", Boolean.FALSE);
            af_properties.put("quality", new Integer(100));
        } catch (IOException ioe) {
            if (DEBUG) {
                System.out.println("FLAC file reader: not a FLAC stream");
            }
            throw new UnsupportedAudioFileException(ioe.getMessage());
        }
        AudioFormat format = new FlacAudioFormat(FlacEncoding.FLAC, sampleRate, bitsPerSample, channels, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false, af_properties);
        if (DEBUG) {
            System.out.println("FLAC file reader: got stream with format " + format);
        }
        return new FlacAudioFileFormat(FlacFileFormatType.FLAC, format, AudioSystem.NOT_SPECIFIED, (int) lFileLengthInBytes, aff_properties);
    }
