    public void processAudio(AudioBuffer buffer) {
        float[] left = buffer.getChannel(0);
        float[] right = buffer.getChannel(1);
        int sampleCount = buffer.getSampleCount();
        int writeCount = 0;
        if (process_buffer == null) nextBuffer();
        while (sampleCount - writeCount != 0) {
            if (process_index_bufferpos == buffersize) nextProcessBuffer();
            int avail = (buffersize - process_index_bufferpos) / 2;
            if (avail > sampleCount - writeCount) avail = sampleCount - writeCount;
            for (int i = 0; i < avail; i++) {
                left[writeCount] += process_buffer[process_index_bufferpos++];
                right[writeCount] += process_buffer[process_index_bufferpos++];
                writeCount++;
            }
        }
    }
