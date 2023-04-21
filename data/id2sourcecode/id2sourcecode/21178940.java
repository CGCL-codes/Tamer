    public void sendMessage() {
        String tempMsg = messageArea.getText().replaceAll("Constants.TEACHER_IDENTIFIER", Constants.BLANK_STRING);
        tempMsg = messageArea.getText().replaceAll(Constants.LINE_BREAK, Constants.BLANK_STRING);
        StaticData.clientMessage = tempMsg;
        Channel channel = session.getChannel(StaticData.channel);
        channel.say(StaticData.clientMessage);
        StaticData.chatMessage = IRCIvelaClientStringUtils.singleton().setMyMessage(StaticData.clientMessage);
        messageArea.setText(Constants.BLANK_STRING);
        messageArea.setFocusable(true);
    }
