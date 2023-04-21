        public int processAudio(AudioBuffer buffer) {
            int n = buffer.getSampleCount();
            if (inPtr + n - outPtr > cacheSize) {
                System.out.println(" OVERFLOW " + overflowCount++);
                return OVERFLOW;
            }
            int inCy0 = (int) (inPtr % cacheSize);
            int inCy1 = (int) ((inPtr + n) % cacheSize);
            if (inCy1 > inCy0) {
                System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n);
            } else {
                System.arraycopy(buffer.getChannel(0), 0, buff, inCy0, n - inCy1);
                System.arraycopy(buffer.getChannel(0), n - inCy1, buff, 0, inCy1);
            }
            inPtr += n;
            if (blocking) {
                if (inPtr - outPtr > required) {
                    itWasMe = true;
                    outBlockingThread.interrupt();
                }
            }
            return AUDIO_OK;
        }
