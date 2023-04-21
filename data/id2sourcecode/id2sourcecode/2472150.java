        private void rawplay(AudioInputStream din, SourceDataLine lin) throws IOException {
            byte[] data = new byte[8192];
            for (; ; ) {
                if (!keepPlaying()) {
                    break;
                }
                int read = din.read(data, 0, data.length);
                if (read < 0) {
                    break;
                } else if (read > 0) {
                    lin.write(data, 0, read);
                } else {
                    sleep(50);
                }
            }
        }
