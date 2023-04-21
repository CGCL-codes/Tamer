    private void doOpenChannel(OpenChannel oc) {
        try {
            ChannelManager cman = AppContext.getChannelManager();
            DataManager dman = AppContext.getDataManager();
            String boundName = Constants.CHANNEL_DATA + oc.channelName;
            Result response = Result.UNKNOWN;
            try {
                dman.getBinding(boundName, ChannelData.class);
                response = Result.ALREADY_EXISTS;
            } catch (NameNotBoundException ex) {
                logger.info(String.format("creating channel '%s'", oc.channelName));
                Channel newChannel = cman.createChannel(oc.channelName, null, Delivery.RELIABLE);
                ChannelData data = new ChannelData(newChannel);
                dman.setBinding(boundName, data);
                if (oc.presences != null) {
                    List<String> presences = Arrays.asList(oc.presences);
                    for (String p : presences) {
                        data.addPresence(new Presence(p, false));
                    }
                }
                logger.info(String.format("instance of %s bound to '%s'", data.getClass().getName(), boundName));
                response = Result.OK;
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
