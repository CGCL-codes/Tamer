    public void sendMessage() {
        String tempMsg = messageArea.getText().replaceAll("Constants.TEACHER_IDENTIFIER", Constants.BLANK_STRING);
        tempMsg = messageArea.getText().replaceAll(Constants.LINE_BREAK, Constants.BLANK_STRING);
        Channel channel = session.getChannel(StaticData.channel);
        session.sayPrivate(title.trim(), tempMsg);
        messageArea.setText(Constants.BLANK_STRING);
        messageArea.setFocusable(true);
    }
