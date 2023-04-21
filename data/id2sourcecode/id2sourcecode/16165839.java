        public void run() {
            int read = 0;
            while (thread != null && state == STATE_RECORDING && read != -1) {
                try {
                    read = audioInputStream.read(buffer, 0, buffer.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (read > 0) {
                    out.write(buffer, 0, read);
                }
            }
        }
