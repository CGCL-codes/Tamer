    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        rl.writeLock().lock();
        try {
            Set s = sessionTable.entrySet();
            Iterator i = s.iterator();
            while (i.hasNext()) {
                Entry m = (Entry) i.next();
                NettyClientServerSession value = (NettyClientServerSession) m.getValue();
                if (e.getChannel().equals(value.getChannel())) {
                    int key = (Integer) m.getKey();
                    sessionTable.remove(key);
                    System.out.println("#Removed client channel with ID= " + key);
                    System.out.println("#active clients=" + sessionTable.size());
                    break;
                }
            }
        } finally {
            rl.writeLock().unlock();
        }
        Logger.println("Session Closed, active clients=" + sessionTable.size());
    }
