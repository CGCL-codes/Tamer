    protected Member[] publishEntryInfo(Object key, Object value) throws ChannelException {
        if (!(key instanceof Serializable && value instanceof Serializable)) return new Member[0];
        Member[] members = getMapMembers();
        int firstIdx = getNextBackupIndex();
        int nextIdx = firstIdx;
        Member[] backup = new Member[0];
        if (members.length == 0 || firstIdx == -1) return backup;
        boolean success = false;
        do {
            Member next = members[nextIdx];
            nextIdx = nextIdx + 1;
            if (nextIdx >= members.length) nextIdx = 0;
            if (next == null) {
                continue;
            }
            MapMessage msg = null;
            try {
                backup = wrap(next);
                msg = new MapMessage(getMapContextName(), MapMessage.MSG_BACKUP, false, (Serializable) key, (Serializable) value, null, channel.getLocalMember(false), backup);
                if (log.isTraceEnabled()) log.trace("Publishing backup data:" + msg + " to: " + next.getName());
                UniqueId id = getChannel().send(backup, msg, getChannelSendOptions());
                if (log.isTraceEnabled()) log.trace("Data published:" + msg + " msg Id:" + id);
                success = true;
            } catch (ChannelException x) {
                log.error("Unable to replicate backup key:" + key + " to backup:" + next + ". Reason:" + x.getMessage(), x);
            }
            try {
                Member[] proxies = excludeFromSet(backup, getMapMembers());
                if (success && proxies.length > 0) {
                    msg = new MapMessage(getMapContextName(), MapMessage.MSG_PROXY, false, (Serializable) key, null, null, channel.getLocalMember(false), backup);
                    if (log.isTraceEnabled()) log.trace("Publishing proxy data:" + msg + " to: " + Arrays.toNameString(proxies));
                    getChannel().send(proxies, msg, getChannelSendOptions());
                }
            } catch (ChannelException x) {
                log.error("Unable to replicate proxy key:" + key + " to backup:" + next + ". Reason:" + x.getMessage(), x);
            }
        } while (!success && (firstIdx != nextIdx));
        return backup;
    }
