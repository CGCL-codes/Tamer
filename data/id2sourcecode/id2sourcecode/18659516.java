    Chat getChannelFor(String jid) {
        String channel = exCN(jid);
        for (int i = 0; i < groupChats.size(); i++) {
            Chat gc = groupChats.get(i);
            if (gc.getName().equals(channel)) {
                return gc;
            }
        }
        throw new NoSuchElementException(jid);
    }
