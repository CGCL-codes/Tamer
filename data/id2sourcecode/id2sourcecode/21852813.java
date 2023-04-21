        public boolean mix(AudioBuffer buffer) {
            float level = controls.getLevel() * 12f;
            ampTracking = controls.getVelocityTrack();
            float bf = getBendFactor();
            if (bendFactor != bf) {
                string.setFrequency(frequency * bf);
                bendFactor = bf;
            }
            float[] samples = buffer.getChannel(0);
            int nsamples = buffer.getSampleCount();
            for (int i = 0; i < nsamples; i++) {
                samples[i] += level * string.getSample();
            }
            return false;
        }
