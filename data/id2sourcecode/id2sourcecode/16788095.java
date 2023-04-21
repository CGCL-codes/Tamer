        public void run() {
            try {
                pos.write(bytes);
                synchronized (this) {
                    notify();
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("Error while running the writer thread.");
            }
        }
