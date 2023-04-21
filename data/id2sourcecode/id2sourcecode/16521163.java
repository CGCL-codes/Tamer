    protected final void processRecord(ChannelRecord record) {
        final ChannelIF channel = record.getChannel();
        observer.pollStarted(channel);
        try {
            if (!record.isFormatResolved()) {
                resolveFormat(record);
            }
            checkContents(record);
            observer.pollFinished(channel);
        } catch (Exception e) {
            observer.channelErrored(channel, e);
        }
    }
