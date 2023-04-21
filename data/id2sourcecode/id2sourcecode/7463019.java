    public static void main(String[] args) throws Exception {
        final DatagramSocket datagramSocket;
        Configuration configuration;
        final Endpoint endpoint;
        Channel channel;
        datagramSocket = new DatagramSocket(11111);
        configuration = new Configuration();
        configuration.setMtu(550);
        endpoint = new StdEndpointImpl(datagramSocket, configuration);
        Thread worker = new Thread() {

            public void run() {
                while (!datagramSocket.isClosed()) endpoint.run();
            }
        };
        worker.start();
        channel = endpoint.connect(new InetSocketAddress("192.168.0.101", 22222));
        byte[] data = new byte[1024 * 1024];
        data[data.length - 1] = '!';
        int total = 0;
        long justNow = System.currentTimeMillis();
        while (total < data.length) {
            int written = channel.write(data, total, data.length - total);
            if (written > 0) {
                total += written;
                while (channel.write(null, 0, 0) != 0) Thread.sleep(100);
                System.out.println(total + " bytes written.");
            } else {
                Thread.sleep(100);
            }
        }
        System.out.println("Finished! " + total + " bytes in " + (System.currentTimeMillis() - justNow) + " millis");
    }
