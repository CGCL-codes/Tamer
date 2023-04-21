    protected void doConnect(String name) throws ConnectionManagerException {
        SelectionKey key = null;
        DatagramChannel dc = null;
        DatagramSocket datagramSocket = null;
        SocketAddress localSocketAddress = null;
        SocketAddress remoteSocketAddress = null;
        Initiator initiator = container.getInitiator(name);
        try {
            dc = DatagramChannel.open();
            dc.configureBlocking(false);
            datagramSocket = dc.socket();
            localSocketAddress = new InetSocketAddress(initiator.getBindAddress(), initiator.getLocalPort());
            remoteSocketAddress = new InetSocketAddress(initiator.getRemoteHost(), initiator.getRemotePort());
            dc.socket().bind(localSocketAddress);
        } catch (IOException ex) {
            container.sendEvent(new InitiatorEvent(EventCatagory.WARN, SystemEventType.EXCEPTION, ex.getLocalizedMessage(), container.getName(), name));
            throw new ConnectionManagerException(ex);
        }
        try {
            key = dc.register(selector, SelectionKey.OP_WRITE, initiator);
            container.sendEvent(new InitiatorEvent(EventCatagory.STATUS, SystemEventType.CONNECT, remoteSocketAddress.toString(), container.getName(), name));
            if (initiator.getBufferSize() > 7) {
                datagramSocket.setReceiveBufferSize(initiator.getBufferSize());
                datagramSocket.setSendBufferSize(initiator.getBufferSize());
            }
            readBuffer = ByteBuffer.allocate(dc.socket().getReceiveBufferSize());
            writeBuffer = ByteBuffer.allocate(dc.socket().getSendBufferSize());
            container.sendEvent(new InitiatorEvent(EventCatagory.INFO, SystemEventType.GENERAL, "init() - initiator.getBufferSize(" + initiator.getBufferSize() + ")\n" + "init() - ReceiveBufferSize(" + datagramSocket.getReceiveBufferSize() + ")\n" + "init() - SendBufferSize(" + datagramSocket.getSendBufferSize() + ")\n" + "init() - readBuffer(" + readBuffer.capacity() + ")\n" + "init() - writeBuffer(" + writeBuffer.capacity() + ")", container.getName(), name));
            doCreateDatagramConnection(key, remoteSocketAddress);
            queueConnectionWrite(dc, remoteSocketAddress);
            initiator.setChannel(dc);
        } catch (IOException ex) {
            container.sendEvent(new InitiatorEvent(EventCatagory.WARN, SystemEventType.EXCEPTION, ex.getLocalizedMessage(), container.getName(), name));
            throw new ConnectionManagerException(ex);
        } catch (InterruptedException ex) {
        }
    }
