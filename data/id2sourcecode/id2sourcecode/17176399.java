        public void run() {
            byte[] buf = new byte[4096];
            int read;
            try {
                while (true) {
                    read = is.read(buf);
                    if (read < 0) {
                        break;
                    }
                    os.write(buf, 0, read);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                }
                synchronized (runningLock) {
                    running = false;
                    runningLock.notifyAll();
                }
            }
        }
