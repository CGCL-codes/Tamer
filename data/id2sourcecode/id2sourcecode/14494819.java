    public int processAudio(AudioBuffer buffer) {
        int nFrame = buffer.getSampleCount();
        int nByte = nFrame * nch * 2;
        if (this.byteBuffer == null || this.byteBuffer.length != nByte) {
            this.byteBuffer = new byte[nByte];
        }
        for (int i = 0; i < buffer.getChannelCount(); i++) {
            bPtr[i] = buffer.getChannel(i);
        }
        try {
            this.reader.read(this.byteBuffer, 0, nByte);
            for (int ch = 0; ch < nch; ch++) {
                for (int n = 0; n < nFrame; n++) {
                    int ptr = (n * nch + ch) * 2;
                    bPtr[ch][n] = ((short) ((0xff & this.byteBuffer[ptr + 0]) + ((0xff & this.byteBuffer[ptr + 1]) * 256)) / 32768f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AUDIO_OK;
    }
