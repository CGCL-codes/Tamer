        private void switchEndian(byte[] b, int off, int len, int readCount) {
            if (sampleSizeInBytes == 2) {
                for (int i = off; i < (off + readCount); i += sampleSizeInBytes) {
                    byte temp;
                    temp = b[i];
                    b[i] = b[i + 1];
                    b[i + 1] = temp;
                }
            }
        }
