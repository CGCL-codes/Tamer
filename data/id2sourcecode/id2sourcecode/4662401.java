    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        log.info("SERVER - messageReceived() " + e);
        Object obj = e.getMessage();
        if (obj instanceof RawFixMessage) {
            RawFixMessage rawFixMessage = (RawFixMessage) obj;
            switch(rawFixMessage.getMessageTypeEnum()) {
                case LOGON:
                    onLogon(rawFixMessage);
            }
            log.info(rawFixMessage.toString());
            Channel channel = e.getChannel();
            if (channel.isWritable()) {
                e.getChannel().write(buffer("OK"));
            }
        }
    }
