    @Override
    public void onOp(OpEvent<CubeIRC> event) throws Exception {
        ChannelOperationEnum co = null;
        if (event.isOp()) co = ChannelOperationEnum.OP; else co = ChannelOperationEnum.DEOP;
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_OPERATION, new ChannelOperationResponse(event.getChannel(), event.getSource(), event.getRecipient(), co));
        super.onOp(event);
    }
