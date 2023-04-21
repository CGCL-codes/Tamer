    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("DNSClient#messageReceived");
        System.out.println(e.getChannel().getLocalAddress() + " | " + e.getChannel().getRemoteAddress());
        this.time = System.currentTimeMillis() - this.time;
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        DNSMessage msg = new DNSMessage(buffer);
        printResult(this.time, msg);
        e.getChannel().close();
    }
