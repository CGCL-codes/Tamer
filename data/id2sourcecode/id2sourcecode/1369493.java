    protected SocketChannel getChannel(String address) {
        SocketChannel sc = (SocketChannel) socketMap.get(address);
        if (sc == null) {
            try {
                String ip = address;
                int port = 777;
                int i = address.indexOf(":");
                if (i != -1) {
                    ip = address.substring(0, i);
                    if (i + 1 < address.length()) port = Integer.parseInt(address.substring(i + 1));
                }
                log.info("Connecting to " + ip + " on port " + port);
                sc = SocketChannel.open();
                sc.connect(new InetSocketAddress(ip, port));
                socketMap.put(address, sc);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return sc;
    }
