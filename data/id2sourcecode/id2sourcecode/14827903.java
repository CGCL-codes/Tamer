    public void deleteGroup() {
        if (group == null) return;
        logger.debug("Deleting Persistent Group: " + group.getTitle());
        synchronized (builder) {
            try {
                builder.beginTransaction();
                builder.reload(group);
                Channel[] chans = (Channel[]) group.getChannels().toArray(new Channel[0]);
                for (int i = 0; i < chans.length; i++) {
                    Channel chan = chans[i];
                    final Set<ChannelGroup> grps = chan.getGroups();
                    grps.remove(group);
                    group.getChannels().remove(chan);
                    if (grps.size() == 0) builder.delete(chan);
                }
                builder.delete(group);
                builder.endTransaction();
                group = null;
            } catch (ChannelBuilderException e) {
                logger.error("Unable to delete Persistent Group: " + e.getMessage());
                builder.resetTransaction();
            }
        }
    }
