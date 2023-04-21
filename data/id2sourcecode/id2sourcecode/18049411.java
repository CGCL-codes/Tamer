        public void run() {
            try {
                byte[] data = new byte[1024];
                int read = -1;
                while (ExecutableController.this.running == null || (running && ExecutableController.this.running.running)) {
                    if (input.available() > 0) {
                        read = input.read(data);
                        if (read == -1) break;
                        process.getOutputStream().write(data, 0, read);
                        process.getOutputStream().flush();
                        if (echo) registry.getStream("process", false).write(data, 0, read);
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                throw ThrowableManagerRegistry.caught(e);
            } finally {
                try {
                    this.input.close();
                } catch (IOException e) {
                    ThrowableManagerRegistry.caught(e);
                }
                this.input = null;
            }
        }
