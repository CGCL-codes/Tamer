    @Override
    public void onVoice(VoiceEvent<CubeIRC> event) throws Exception {
        ChannelOperationEnum co = null;
        if (event.hasVoice()) co = ChannelOperationEnum.VOICE; else co = ChannelOperationEnum.DEVOICE;
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_OPERATION, new ChannelOperationResponse(event.getChannel(), event.getSource(), event.getRecipient(), co));
        super.onVoice(event);
    }
