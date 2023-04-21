        @Override
        public void processMetadata(Metadata meta) {
            if (meta instanceof StreamInfo) {
                StreamInfo si = (StreamInfo) meta;
                duration = si.getTotalSamples() / si.getSampleRate();
                channels = si.getChannels();
                bitrate = si.getBitsPerSample();
            }
        }
