        public int processAudio(AudioBuffer buffer) {
            int n = buffer.getSampleCount();
            int nchan = buffer.getChannelCount();
            float Fs = buffer.getSampleRate();
            for (int i = 0; i < n; i++) {
                float val = (float) Math.sin((2 * Math.PI * freq * i) / Fs);
                for (int chan = 0; chan < nchan; chan++) {
                    buffer.getChannel(chan)[i] = val;
                }
            }
            return AUDIO_OK;
        }
