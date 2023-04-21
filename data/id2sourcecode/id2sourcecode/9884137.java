    private void doRemovePresence(RemovePresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result response = Result.UNKNOWN;
            if (channelData == null) {
                response = Result.NOT_FOUND;
            } else {
                Presence p = new Presence(msg.presenceName, false);
                channelData.removePresence(p);
                response = Result.OK;
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
