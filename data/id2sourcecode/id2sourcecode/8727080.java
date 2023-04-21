    public TaskChannelSender(final IMessage msgToSend) {
        super(msgToSend);
        String chType = ChannelTypeForMsgTypes.getInstance().getChannelTypeForMsgType(msgToSend.getType());
        this.setChannelType(chType);
    }
