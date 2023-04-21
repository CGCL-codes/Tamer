        public void work(int bufSize) {
            read(bytebuffer);
            float[] left = buffer.getChannel(0);
            float[] right = buffer.getChannel(1);
            for (int n = 0; n < buffer.getSampleCount() * 2; n++) {
                float sample = ((short) ((0xff & bytebuffer[(n * 2) + 1]) + ((0xff & bytebuffer[(n * 2) + 0]) * 256)) / 32768f);
                if (n % 2 == 0) left[n / 2] = sample; else right[n / 2] = sample;
            }
            outputprocess.processAudio(buffer);
        }
