    private void doAddPresence(AddPresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result response = Result.UNKNOWN;
            if (channelData == null) {
                response = Result.NOT_FOUND;
            } else {
                Presence p = new Presence(msg.presenceName, false);
                boolean succeeded = channelData.addPresence(p);
                if (succeeded) {
                    response = Result.OK;
                } else {
                    response = Result.ALREADY_EXISTS;
                }
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
