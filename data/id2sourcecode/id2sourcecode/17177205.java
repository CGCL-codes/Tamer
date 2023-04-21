    public List<TriggerRouter> select() {
        List<TriggerRouter> filtered = new ArrayList<TriggerRouter>();
        for (TriggerRouter trigger : triggers) {
            if (trigger.getTrigger().getChannelId().equals(channelId) && (targetNodeGroupId == null || trigger.getRouter().getNodeGroupLink().getTargetNodeGroupId().equals(targetNodeGroupId))) {
                filtered.add(trigger);
            }
        }
        return filtered;
    }
