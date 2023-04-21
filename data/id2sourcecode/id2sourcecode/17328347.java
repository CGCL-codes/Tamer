        public void run() {
            try {
                while (running) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    while (System.in.available() > 0) {
                        processStdIn.write(System.in.read());
                        processStdIn.flush();
                    }
                }
            } catch (IOException e) {
            }
        }
