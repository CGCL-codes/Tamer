    public void buildNotifications() {
        if (!bDirty) {
            return;
        }
        Set<Integer> chs = WorldRegistryImpl.getInstance().getChannelServer();
        if (notifications.keySet().size() != chs.size()) {
            notifications.clear();
            for (Integer ch : chs) {
                notifications.put(ch, new java.util.LinkedList<Integer>());
            }
        } else {
            for (List<Integer> l : notifications.values()) {
                l.clear();
            }
        }
        synchronized (members) {
            for (MapleGuildCharacter mgc : members) {
                if (!mgc.isOnline()) {
                    continue;
                }
                List<Integer> ch = notifications.get(mgc.getChannel());
                if (ch == null) {
                    log.warn("Unable to connect to channel " + mgc.getChannel());
                } else {
                    ch.add(mgc.getId());
                }
            }
        }
        bDirty = false;
    }
