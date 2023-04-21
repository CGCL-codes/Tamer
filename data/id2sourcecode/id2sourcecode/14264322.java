    public void handle(GetChannelResponse r) {
        SocketChannel ch = r.getChannel();
        Integer key = new Integer(ch.hashCode());
        channels.put(key, ch);
        Collection list = (Collection) pending.get(key);
        if (null != list && 0 < list.size()) {
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                handle((Packet) it.next());
            }
        }
    }
