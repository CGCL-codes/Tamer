        public int processAudio(AudioBuffer buffer) {
            int n = buffer.getSampleCount();
            int inCy0 = (int) (inPtr % cacheSize);
            int inCy1 = (int) ((inPtr + n) % cacheSize);
            synchronized (muex) {
                if (inCy1 > inCy0) {
                    System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n);
                } else {
                    System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n - inCy1);
                    System.arraycopy(buffer.getChannel(0), n - inCy1, buff, 0, inCy1);
                }
                inPtr += n;
            }
            return AUDIO_OK;
        }
