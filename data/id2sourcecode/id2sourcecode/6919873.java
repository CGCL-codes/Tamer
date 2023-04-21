    protected void sendDataFromForwarderToClient(final ChannelBuffer bufferedMessage) {
        Channel channel = Server.getAllChannels().find(pnlClientsBar1.getSelectedClient());
        if (channel != null) {
            ChannelFuture future = channel.write(bufferedMessage);
            future.addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        mirrorBinaryData(bufferedMessage, pnlSentData);
                    } else {
                        pnlSystemLog.addLog("Some data tranmission error while data sending to client number: " + future.getChannel().getId() + " occure.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
                    }
                }
            });
        } else {
            pnlSystemLog.addLog("No client to send data selected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
        }
    }
