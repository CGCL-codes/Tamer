    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent evt) throws Exception {
        MudSession session = new MudSession(ctx.getChannel());
        ctx.setAttachment(session);
        TaskQueue.enqueue(new SessionOpenTask(session));
    }
