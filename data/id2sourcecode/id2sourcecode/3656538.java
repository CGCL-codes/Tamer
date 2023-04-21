    MessageAttachments getAttachmentsForChannel(String channelName) {
        MessageAttachments ret = new MessageAttachments();
        Iterator itr = iterator();
        while (itr.hasNext()) {
            DeviceMessageAttachment dma = (DeviceMessageAttachment) itr.next();
            try {
                if (dma.getValueType() != MessageAttachment.UNDEFINED) {
                    if (dma.getChannelName().equals(channelName) || dma.getChannelName() == null) {
                        ret.addAttachment(dma);
                    }
                }
            } catch (MessageException mse) {
                logger.error("channel-message-list-error", channelName, mse);
            }
        }
        return ret;
    }
