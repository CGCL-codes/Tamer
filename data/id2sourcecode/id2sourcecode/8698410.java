    public void handle(AddChannelCommand add) {
        LOG.info("Received new channel");
        SocketChannel ch = add.getChannel();
        channels.add(ch);
        getContext().send(add);
    }
