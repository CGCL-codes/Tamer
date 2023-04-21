        public SRCAudioFormat(AudioFormat targetFormat) {
            super(targetFormat.getEncoding(), targetFormat.getSampleRate(), targetFormat.getSampleSizeInBits(), targetFormat.getChannels(), AudioUtils.getFrameSize(targetFormat.getChannels(), targetFormat.getSampleSizeInBits()), targetFormat.getSampleRate(), targetFormat.isBigEndian(), targetFormat.properties());
            this.sampleRate = targetFormat.getSampleRate();
        }
