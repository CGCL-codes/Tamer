    public synchronized boolean openChannel(Channel channel, ChannelEventListener eventListener) throws IOException {
        Long channelId = getChannelId();
        SshMsgChannelOpen msg = new SshMsgChannelOpen(channel.getChannelType(), channelId.longValue(), channel.getLocalWindow().getWindowSpace(), channel.getLocalPacketSize(), channel.getChannelOpenData());
        transport.sendMessage(msg, this);
        int[] messageIdFilter = new int[2];
        messageIdFilter[0] = SshMsgChannelOpenConfirmation.SSH_MSG_CHANNEL_OPEN_CONFIRMATION;
        messageIdFilter[1] = SshMsgChannelOpenFailure.SSH_MSG_CHANNEL_OPEN_FAILURE;
        try {
            SshMessage result = messageStore.getMessage(messageIdFilter);
            if (result.getMessageId() == SshMsgChannelOpenConfirmation.SSH_MSG_CHANNEL_OPEN_CONFIRMATION) {
                SshMsgChannelOpenConfirmation conf = (SshMsgChannelOpenConfirmation) result;
                activeChannels.put(channelId, channel);
                log.debug("Initiating channel");
                channel.init(this, channelId.longValue(), conf.getSenderChannel(), conf.getInitialWindowSize(), conf.getMaximumPacketSize(), eventListener);
                channel.open();
                log.info("Channel " + String.valueOf(channel.getLocalChannelId()) + " is open [" + channel.getName() + "]");
                return true;
            } else {
                channel.getState().setValue(ChannelState.CHANNEL_CLOSED);
                return false;
            }
        } catch (MessageStoreEOFException mse) {
            throw new IOException(mse.getMessage());
        } catch (InterruptedException ex) {
            throw new SshException("The thread was interrupted whilst waiting for a connection protocol message");
        }
    }
