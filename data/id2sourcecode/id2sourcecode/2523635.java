    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        log.trace("Message received from channel " + e.getChannel().getId());
        ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(channelBuffer.array());
        while (in.available() > 0) {
            RougeObject resp = BDecoder.bDecode(in);
            String command = resp.getString("command");
            RougeObject payload = resp.getRougeObject("payload");
            this.driver.handle(command, payload);
        }
    }
