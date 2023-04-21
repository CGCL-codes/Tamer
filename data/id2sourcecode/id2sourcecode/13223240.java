        public int read(float[] b, int off, int len) throws IOException {
            int len2 = (len / targetChannels) * sourceChannels;
            if (conversion_buffer == null || conversion_buffer.length < len2) conversion_buffer = new float[len2];
            int ret = ais.read(conversion_buffer, 0, len2);
            if (ret < 0) return ret;
            if (sourceChannels == 1) {
                int cs = targetChannels;
                for (int c = 0; c < targetChannels; c++) {
                    for (int i = 0, ix = off + c; i < len2; i++, ix += cs) {
                        b[ix] = conversion_buffer[i];
                        ;
                    }
                }
            } else if (targetChannels == 1) {
                int cs = sourceChannels;
                for (int i = 0, ix = off; i < len2; i += cs, ix++) {
                    b[ix] = conversion_buffer[i];
                }
                for (int c = 1; c < sourceChannels; c++) {
                    for (int i = c, ix = off; i < len2; i += cs, ix++) {
                        b[ix] += conversion_buffer[i];
                        ;
                    }
                }
                float vol = 1f / ((float) sourceChannels);
                for (int i = 0, ix = off; i < len2; i += cs, ix++) {
                    b[ix] *= vol;
                }
            } else {
                int minChannels = Math.min(sourceChannels, targetChannels);
                int off_len = off + len;
                int ct = targetChannels;
                int cs = sourceChannels;
                for (int c = 0; c < minChannels; c++) {
                    for (int i = off + c, ix = c; i < off_len; i += ct, ix += cs) {
                        b[i] = conversion_buffer[ix];
                    }
                }
                for (int c = minChannels; c < targetChannels; c++) {
                    for (int i = off + c; i < off_len; i += ct) {
                        b[i] = 0;
                    }
                }
            }
            return (ret / sourceChannels) * targetChannels;
        }
