        public boolean mix(AudioBuffer buffer) {
            if (stop) return false;
            float[] samples = buffer.getChannel(0);
            int nsamples = buffer.getSampleCount();
            for (int i = 0; i < nsamples; i++) {
                samples[i] += getSample();
            }
            return !isComplete();
        }
