    public void moveChannelTo(Channel channel, PersistChanGrpMgr destGrp) {
        if (activated || destGrp.isActivated()) throw new IllegalStateException("can't move Channels while activated.");
        synchronized (builder) {
            try {
                builder.beginTransaction();
                builder.reload(group);
                builder.reload(channel);
                ChannelGroup dstGroup = builder.reload(destGrp.getChannelGroup());
                group.remove(channel);
                channel.getGroups().remove(group);
                dstGroup.add(channel);
                channel.getGroups().add(dstGroup);
                builder.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                builder.resetTransaction();
            }
        }
    }
