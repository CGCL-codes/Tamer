    protected final void processRecord(ChannelRecord record) {
        if (matcher != null && observer != null) {
            final ChannelIF channel = record.getChannel();
            observer.cleaningStarted(channel);
            final ItemIF[] items = (ItemIF[]) channel.getItems().toArray(new ItemIF[0]);
            for (int i = 0; i < items.length; i++) {
                checkItem(items[i], channel);
            }
            observer.cleaningFinished(channel);
        }
    }
