        public int computeLength(long duration, Format fmt) {
            if (!(fmt instanceof AudioFormat)) return -1;
            AudioFormat af = (AudioFormat) fmt;
            return (int) ((((duration / 1000) * (af.getChannels() * af.getSampleSizeInBits())) / 1000) * af.getSampleRate() / 8000);
        }
