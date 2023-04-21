        public void run() {
            byte[] buffer = new byte[1024];
            try {
                do {
                    try {
                        OutputStream target = registry.getStream("process", false);
                        if (target != null) {
                            int read = (process.getInputStream().read(buffer));
                            if (read != -1) target.write(buffer, 0, read); else break;
                        } else {
                            ThreadUtil.sleep(100);
                        }
                    } catch (Exception e) {
                        ThrowableManagerRegistry.caught(e);
                    }
                } while (running.running || process.getInputStream().available() != 0);
            } catch (IOException e) {
                throw ThrowableManagerRegistry.caught(e);
            }
        }
