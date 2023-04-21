        public synchronized int processAudio(AudioBuffer buffer) {
            required = buffer.getSampleCount();
            if (inPtr - outPtr < required) {
                outBlockingThread = Thread.currentThread();
                try {
                    while (Thread.currentThread().interrupted()) ;
                    blocking = true;
                    wait();
                } catch (InterruptedException e) {
                    blocking = false;
                    if (itWasMe) {
                        itWasMe = false;
                    } else {
                        e.printStackTrace();
                        e.getCause().printStackTrace();
                        inPtr = 0;
                        outPtr = 0;
                        itWasMe = false;
                        return AudioFlags.INTERRUPTED;
                    }
                }
                assert (inPtr - outPtr >= required);
            }
            int outCy0 = (int) (outPtr % cacheSize);
            int outCy1 = (int) ((outPtr + required) % cacheSize);
            if (outCy1 > outCy0) {
                System.arraycopy(buff, outCy0, buffer.getChannel(0), 0, required);
            } else {
                System.arraycopy(buff, outCy0, buffer.getChannel(0), 0, required - outCy1);
                System.arraycopy(buff, 0, buffer.getChannel(0), required - outCy1, outCy1);
            }
            outPtr += required;
            return AUDIO_OK;
        }
