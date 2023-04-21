    public void poll() throws IOException {
        synchronized (this) {
            if (mode != MODE_PLAYING) {
                return;
            }
        }
        if (audioCapture.isUsed()) {
            return;
        }
        if (written >= read) {
            try {
                read = audioInputStream.read(buffer, 0, buffer.length);
                written = 0;
            } catch (IOException e) {
                log.warning("Sound: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        if (read > written) {
            int temp = line.write(buffer, written, read - written);
            written += temp;
        }
        if (read == -1) {
            stopFilePlayback();
            synchronized (this) {
                mode = MODE_READY;
            }
        }
    }
