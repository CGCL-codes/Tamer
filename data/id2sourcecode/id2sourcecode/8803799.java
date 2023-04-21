    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent msg) {
        String mess = (String) msg.getMessage();
        logger.info("handler receive message=" + mess);
        if (mess.startsWith(Constants.FLASH_REQUEST)) {
            byte[] reps = null;
            try {
                reps = Constants.FLASH_ANSWER.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ChannelBuffer wb = ChannelBuffers.directBuffer(reps.length);
            wb.writeBytes(reps);
            Channels.write(ctx, msg.getFuture(), wb);
            return;
        }
        long t = System.currentTimeMillis();
        if (mess.trim().equals("") || !mess.contains("@")) {
            logger.error("error message:" + mess);
            return;
        }
        Channel channel = ctx.getChannel();
        Session session = NettySession.getInstance(channel);
        GameMessage message = GameMessage.getInstance(channel.getId(), mess);
        if (message.getMiddleServer()) {
            this.executeMiddelMessagte(message, session);
        } else this.executeMessage(message, session);
        long t2 = (System.currentTimeMillis() - t);
        if (t2 > 500) {
            logger.info("message=" + mess);
            logger.info("execute time=" + t2);
        }
    }
