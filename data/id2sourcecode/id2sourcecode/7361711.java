        public void run() {
            try {
                byte[] data = new byte[9046];
                int read = -1;
                while (running) {
                    if (input.available() > 0) {
                        read = input.read(data);
                        if (read == -1) break;
                        if (buffer != null) {
                            buffer = buffer.write(data, 0, read);
                        } else break;
                    } else Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                throw ThrowableManagerRegistry.caught(e);
            } finally {
                this.input = null;
            }
        }
