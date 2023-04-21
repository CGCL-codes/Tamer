    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ImapMessage message = (ImapMessage) e.getMessage();
        if (throttleIO) {
            if (!e.getChannel().isWritable()) {
                e.getChannel().setReadable(false);
            }
        }
        ImapSession session = (ImapSession) ctx.getAttachment();
        ImapRequest request = ImapRequestFactory.createImapRequest(message);
        ImapProcessor processor = ImapProcessorFactory.createImapProcessor(request);
        processor.process(session, request, e.getChannel());
    }
