    @Override
    public synchronized void write() throws InterruptedException {
        if (!dataLine.isActive()) {
            dataLine.start();
        }
        final int len;
        if (fastForward > 1) {
            sampleBuffer.rewind();
            int newLen = 0;
            int[] val = new int[audioFormat.getChannels()];
            int j = 0;
            while (sampleBuffer.position() < sampleBuffer.capacity()) {
                for (int c = 0; c < audioFormat.getChannels(); c++) {
                    val[c] += sampleBuffer.getShort();
                }
                j++;
                if (j == fastForward) {
                    j = 0;
                    for (int c = 0; c < audioFormat.getChannels(); c++) {
                        sampleBuffer.putShort(newLen, (short) (val[c] / fastForward));
                        newLen += 2;
                    }
                    Arrays.fill(val, 0);
                }
            }
            len = newLen;
        } else {
            len = sampleBuffer.capacity();
        }
        int bytesWritten = dataLine.write(sampleBuffer.array(), 0, len);
        if (bytesWritten != len) {
            throw new InterruptedException();
        }
    }
