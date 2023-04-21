    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object o = e.getMessage();
        if (o instanceof ChannelBuffer) {
            ChannelBuffer c = (ChannelBuffer) o;
            byte[] b = new byte[c.capacity()];
            c.getBytes(0, b);
            logger.info("RTSP Request \n" + new String(b));
            return;
        }
        HttpRequest rtspRequest = (HttpRequest) e.getMessage();
        if (rtspRequest.getMethod().equals(HttpMethod.POST)) {
            if (logger.isInfoEnabled()) {
                logger.info("Received the POST Request. Changing the PipeLine");
            }
            ChannelPipeline p = ctx.getChannel().getPipeline();
            p.replace("decoder", "base64Decoder", new Base64Decoder());
        }
        rtspServerStackImpl.processRtspRequest(rtspRequest, e.getChannel());
    }
