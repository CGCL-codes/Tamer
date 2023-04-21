    public Object get(Object key) {
        MapEntry entry = (MapEntry) super.get(key);
        if (log.isTraceEnabled()) log.trace("Requesting id:" + key + " entry:" + entry);
        if (entry == null) return null;
        if (!entry.isPrimary()) {
            try {
                Member[] backup = null;
                MapMessage msg = null;
                if (!entry.isBackup()) {
                    msg = new MapMessage(getMapContextName(), MapMessage.MSG_RETRIEVE_BACKUP, false, (Serializable) key, null, null, null, null);
                    Response[] resp = getRpcChannel().send(entry.getBackupNodes(), msg, this.getRpcChannel().FIRST_REPLY, Channel.SEND_OPTIONS_DEFAULT, getRpcTimeout());
                    if (resp == null || resp.length == 0) {
                        log.warn("Unable to retrieve remote object for key:" + key);
                        return null;
                    }
                    msg = (MapMessage) resp[0].getMessage();
                    msg.deserialize(getExternalLoaders());
                    backup = entry.getBackupNodes();
                    if (entry.getValue() instanceof ReplicatedMapEntry) {
                        ReplicatedMapEntry val = (ReplicatedMapEntry) entry.getValue();
                        val.setOwner(getMapOwner());
                    }
                    if (msg.getValue() != null) entry.setValue(msg.getValue());
                }
                if (entry.isBackup()) {
                    backup = publishEntryInfo(key, entry.getValue());
                } else if (entry.isProxy()) {
                    msg = new MapMessage(getMapContextName(), MapMessage.MSG_PROXY, false, (Serializable) key, null, null, channel.getLocalMember(false), backup);
                    Member[] dest = getMapMembersExcl(backup);
                    if (dest != null && dest.length > 0) {
                        getChannel().send(dest, msg, getChannelSendOptions());
                    }
                }
                entry.setPrimary(channel.getLocalMember(false));
                entry.setBackupNodes(backup);
                entry.setBackup(false);
                entry.setProxy(false);
            } catch (Exception x) {
                log.error("Unable to replicate out data for a LazyReplicatedMap.get operation", x);
                return null;
            }
        }
        if (log.isTraceEnabled()) log.trace("Requesting id:" + key + " result:" + entry.getValue());
        if (entry.getValue() != null && entry.getValue() instanceof ReplicatedMapEntry) {
            ReplicatedMapEntry val = (ReplicatedMapEntry) entry.getValue();
            val.setOwner(getMapOwner());
        }
        return entry.getValue();
    }
