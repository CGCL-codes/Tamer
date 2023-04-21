    @SuppressWarnings("unchecked")
    @FromState("NetworkRunning")
    @OnMessage(performative = "InformRef")
    public WorkflowState onNewData(Message msg) throws Exception {
        Object channelId = getPart(msg, CONTENT, "channel");
        if (channelId != null) {
            DistributedChannel<Object> ch = (DistributedChannel<Object>) getChannel(channelId);
            Map<String, Object> rep = getPart(msg, CONTENT, "datum");
            ch.putLocal(fromJson(rep));
            reply(msg, Performative.Confirm, null);
        }
        return null;
    }
