    public void run() {
        FileChannel channel = null;
        try {
            channel = new FileInputStream("/dev/zero").getChannel();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (channel == null) {
            return;
        }
        System.out.println("KernelTest thread " + id + " started");
        for (; ; ) {
            try {
                _theBuffer.clear();
                channel.read(_theBuffer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
