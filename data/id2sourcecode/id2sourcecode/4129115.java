    public void handle(AddChannelCommand add) {
        LOG.info("Got new channel");
        SocketChannel ch = add.getChannel();
        int before = selector.keys().size();
        try {
            ch.configureBlocking(false);
            SelectionKey key = ch.register(selector, SelectionKey.OP_READ);
            key.attach(ch);
        } catch (ClosedChannelException e) {
            getContext().respond(e);
        } catch (IOException e) {
            getContext().respond(e);
        }
        int after = selector.keys().size();
        if (0 == before && 0 < after) {
            try {
                getPort().send(getContext().createMessage(READ_PACKETS));
            } catch (PortException e1) {
                LOG.error("Failed to send READ_PACKETS message", e1);
            }
        }
    }
