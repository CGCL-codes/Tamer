    @Listener("/service/stream/get")
    public void processStream(final ServerSession remote, final ServerMessage.Mutable message) {
        final Map<String, Object> output = new HashMap<String, Object>();
        try {
            log.debug("ActivityStreamService............");
            final List<UtilNotification> activities = getStreamOperations().retrieveLastNotifications(20, false, null);
            log.debug("not stream SIZE...." + activities.size());
            output.put("stream", JSONUtils.convertObjectToJsonString(activities));
        } catch (Exception e) {
            log.fatal("cometd error: " + e.getMessage());
            output.put("stream", ListUtils.EMPTY_LIST);
        }
        remote.deliver(getServerSession(), message.getChannel(), output, null);
    }
