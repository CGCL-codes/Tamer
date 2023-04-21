    public Object remove(Object key, boolean notify) {
        MapEntry entry = (MapEntry) super.remove(key);
        try {
            if (getMapMembers().length > 0 && notify) {
                MapMessage msg = new MapMessage(getMapContextName(), MapMessage.MSG_REMOVE, false, (Serializable) key, null, null, null, null);
                getChannel().send(getMapMembers(), msg, getChannelSendOptions());
            }
        } catch (ChannelException x) {
            log.error("Unable to replicate out data for a LazyReplicatedMap.remove operation", x);
        }
        return entry != null ? entry.getValue() : null;
    }
