    protected void sendDataFromForwarderOutAndLogIt(ChannelBuffer msg) {
        Globals.getInstance().getForwarder().getChannel().write(msg);
        mirrorBinaryData(msg, pnlSentData);
    }
