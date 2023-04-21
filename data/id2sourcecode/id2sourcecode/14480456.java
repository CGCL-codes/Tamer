    public void mapMemberAdded(Member member) {
        if (member.equals(getChannel().getLocalMember(false))) return;
        boolean memberAdded = false;
        synchronized (mapMembers) {
            if (!mapMembers.containsKey(member)) {
                mapMembers.put(member, new Long(System.currentTimeMillis()));
                memberAdded = true;
            }
        }
        if (memberAdded) {
            synchronized (stateMutex) {
                Iterator i = super.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = (Map.Entry) i.next();
                    MapEntry entry = (MapEntry) super.get(e.getKey());
                    if (entry == null) continue;
                    if (entry.isPrimary() && (entry.getBackupNodes() == null || entry.getBackupNodes().length == 0)) {
                        try {
                            Member[] backup = publishEntryInfo(entry.getKey(), entry.getValue());
                            entry.setBackupNodes(backup);
                            entry.setPrimary(channel.getLocalMember(false));
                        } catch (ChannelException x) {
                            log.error("Unable to select backup node.", x);
                        }
                    }
                }
            }
        }
    }
