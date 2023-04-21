        public void run() {
            try {
                pos.write(42);
                pos.close();
                while (keepRunning) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("Error while running the writer thread.");
            }
        }
