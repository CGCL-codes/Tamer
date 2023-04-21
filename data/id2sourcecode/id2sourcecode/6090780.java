        public void run() {
            System.out.println("thread-starting");
            byte[] buf = new byte[256];
            int n = 0;
            while (n >= 0) {
                try {
                    n = telnetWrapper.read(buf);
                    if (n > 0) {
                        out.print(new String(buf, 0, n));
                    }
                } catch (IOException e) {
                    System.out.println("ReaderThread.run: got exception in read/write loop: " + e);
                    e.printStackTrace();
                    return;
                }
            }
        }
