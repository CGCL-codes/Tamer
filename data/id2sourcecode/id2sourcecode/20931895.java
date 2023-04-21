    public synchronized boolean sendChannelRequest(Channel channel, String requestType, boolean wantReply, byte[] requestData) throws IOException {
        boolean success = true;
        log.info("Sending " + requestType + " request for the " + channel.getChannelType() + " channel");
        SshMsgChannelRequest msg = new SshMsgChannelRequest(channel.getRemoteChannelId(), requestType, wantReply, requestData);
        transport.sendMessage(msg, this);
        if (wantReply) {
            int[] messageIdFilter = new int[2];
            messageIdFilter[0] = SshMsgChannelSuccess.SSH_MSG_CHANNEL_SUCCESS;
            messageIdFilter[1] = SshMsgChannelFailure.SSH_MSG_CHANNEL_FAILURE;
            log.info("Waiting for channel request reply");
            try {
                SshMessage reply = messageStore.getMessage(messageIdFilter);
                switch(reply.getMessageId()) {
                    case SshMsgChannelSuccess.SSH_MSG_CHANNEL_SUCCESS:
                        {
                            log.info("Channel request succeeded");
                            success = true;
                            break;
                        }
                    case SshMsgChannelFailure.SSH_MSG_CHANNEL_FAILURE:
                        {
                            log.info("Channel request failed");
                            success = false;
                            break;
                        }
                }
            } catch (InterruptedException ex) {
                throw new SshException("The thread was interrupted whilst waiting for a connection protocol message");
            }
        }
        return success;
    }
