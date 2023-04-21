    @Override
    public void open(final AudioConfig cfg) throws LineUnavailableException {
        super.open(cfg);
        jump3r = new LameDecoder(mp3File.getAbsolutePath());
        mp3Driver.open(new AudioConfig(jump3r.getSampleRate(), jump3r.getChannels(), SamplingMethod.RESAMPLE) {

            @Override
            public int getChunkFrames() {
                return jump3r.getFrameSize();
            }

            @Override
            public int getFrameRate() {
                return jump3r.getSampleRate();
            }
        });
    }
