    protected void doBind(String name) throws IOException {
        DatagramChannel dc = null;
        DatagramSocket datagramSocket = null;
        SocketAddress localSocketAddress = null;
        Listener listener = container.getListener(name);
        try {
            dc = DatagramChannel.open();
            dc.configureBlocking(false);
            datagramSocket = dc.socket();
            localSocketAddress = new InetSocketAddress(listener.getBindAddress(), listener.getLocalPort());
            datagramSocket.bind(localSocketAddress);
            dc.register(selector, SelectionKey.OP_READ, listener);
            if (listener.getBufferSize() > 7) {
                datagramSocket.setReceiveBufferSize(listener.getBufferSize());
                datagramSocket.setSendBufferSize(listener.getBufferSize());
            }
            readBuffer = ByteBuffer.allocate(datagramSocket.getReceiveBufferSize());
            writeBuffer = ByteBuffer.allocate(datagramSocket.getSendBufferSize());
            container.sendEvent(new ConnectionEvent(EventCatagory.STATUS, SystemEventType.BIND, "bind", container.getName(), null, name));
            container.sendEvent(new ConnectionEvent(EventCatagory.INFO, SystemEventType.GENERAL, "init() - listener.getBufferSize(" + listener.getBufferSize() + ")\n" + "init() - ReceiveBufferSize(" + datagramSocket.getReceiveBufferSize() + ")\n" + "init() - SendBufferSize(" + datagramSocket.getSendBufferSize() + ")\n" + "init() - readBuffer(" + readBuffer.capacity() + ")\n" + "init() - writeBuffer(" + writeBuffer.capacity() + ")", container.getName(), null, name));
            listener.setChannel(dc);
        } catch (IOException ex) {
            container.sendEvent(new ConnectionEvent(EventCatagory.WARN, SystemEventType.EXCEPTION, ex.getLocalizedMessage(), container.getName(), null, name));
        }
    }
