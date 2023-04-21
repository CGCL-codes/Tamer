        public AudioFloatInputStreamChannelMixer(AudioFloatInputStream ais, int targetChannels) {
            this.sourceChannels = ais.getFormat().getChannels();
            this.targetChannels = targetChannels;
            this.ais = ais;
            AudioFormat format = ais.getFormat();
            targetFormat = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), targetChannels, (format.getFrameSize() / sourceChannels) * targetChannels, format.getFrameRate(), format.isBigEndian());
        }
