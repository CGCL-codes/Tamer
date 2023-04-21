        public void run() {
            while (thread != null && state == STATE_PLAYING && read != -1) {
                if (written >= read) {
                    try {
                        read = audioInputStream.read(buffer, 0, buffer.length);
                        written = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (read > written) {
                    int temp = line.write(buffer, written, read - written);
                    written += temp;
                }
            }
            if (state == STATE_PLAYING) {
                line.drain();
                line.close();
                stopButton.doClick();
            }
        }
