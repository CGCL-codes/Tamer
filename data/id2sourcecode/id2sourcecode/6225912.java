    private void connectSlaves(List<SlaveAddress> slaveList, List<Socket> socketList) throws IOException, UnknownHostException, ClosedChannelException {
        for (SlaveAddress nodeAddress : slaveList) {
            Socket socket = OhuaSocketFactory.getInstance().createSocket(nodeAddress._IP, nodeAddress._port);
            socketList.add(socket);
            socket.getChannel().register(_selector, SelectionKey.OP_READ, nodeAddress);
        }
    }
