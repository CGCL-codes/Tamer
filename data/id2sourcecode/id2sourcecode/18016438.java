        public Normalizer(AudioFormat pFormat) {
            audioFormat = pFormat;
            channels = new float[pFormat.getChannels()][];
            for (int c = 0; c < pFormat.getChannels(); c++) {
                channels[c] = new float[sampleSize];
            }
            channelSize = audioFormat.getFrameSize() / audioFormat.getChannels();
            audioSampleSize = (1 << (audioFormat.getSampleSizeInBits() - 1));
        }
