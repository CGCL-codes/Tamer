    public static void main(String[] args) throws Exception {
        final DatagramSocket datagramSocket;
        Configuration configuration;
        final Endpoint endpoint;
        Channel channel;
        BlockingChannel blockingChannel;
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
        blockingChannel = new BlockingChannel(channel, 8000);
        byte[] data = new byte[1024 * 1024];
        data[data.length - 1] = '!';
        long justNow = System.currentTimeMillis();
        int read = blockingChannel.write(data, 0, data.length);
        System.out.println("Finished! " + read + " bytes in " + (System.currentTimeMillis() - justNow) + " millis");
    }
