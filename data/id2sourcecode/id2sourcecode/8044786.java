    private void scheduler_loadAllClassrooms(final Map<db.Channel, Set<Presence>> classroomMap, final VoidCallback callback) {
        try {
            ChannelManager cman = AppContext.getChannelManager();
            DataManager dman = AppContext.getDataManager();
            for (db.Channel c : classroomMap.keySet()) {
                Set<Presence> presenceSet = classroomMap.get(c);
                Channel newChannel = cman.createChannel(c.getChannelName(), null, Delivery.RELIABLE);
                String boundName = Constants.CHANNEL_DATA + newChannel.getName();
                ChannelData data = new ChannelData(newChannel);
                data.addPresences(presenceSet);
                dman.setBinding(boundName, data);
                logger.info(String.format("bound '%s' to '%s'", newChannel.getName(), boundName));
            }
            if (callback != null) {
                callback.callback();
            }
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            if (callback != null) {
                callback.exceptionThrown(ex);
            }
        }
    }
