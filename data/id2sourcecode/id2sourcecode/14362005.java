    private static void send(String address, int port) throws InterruptedException {
        UdpPublisher pub = new UdpPublisher(address, port);
        pub.connect();
        BatchWriter writer = new BatchWriter(1024 * 25, pub, pub.getAddress(), pub.getPort());
        writer.setAfterFlushSleep(0);
        Thread thread = new Thread(writer);
        thread.start();
        int count = 0;
        long startTime = System.currentTimeMillis();
        byte[] bytes = new byte[0];
        while (true) {
            count++;
            writer.publish("test", bytes, 0, bytes.length);
            if ((count % 500000) == 0) {
                long totalTime = System.currentTimeMillis() - startTime;
                System.out.println("count: " + count);
                double avg = count / (totalTime / 1000.00);
                System.out.println("avg/sec: " + avg);
                count = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
