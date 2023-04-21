    private void checkContents(ChannelRecord record) throws IOException, ParseException {
        final ChannelIF channel = record.getChannel();
        URL baseUrl = channel.getLocation();
        InputStream in = getInputStream(channel, "Fetching");
        if (in != null) {
            try {
                ChannelIF tempChannel = FeedParser.parse(BUILDER, createInputSource(in), baseUrl);
                if (!record.isCanceled() && channelHasChanged(channel, tempChannel)) {
                    InformaUtils.copyChannelProperties(tempChannel, channel);
                    observer.channelChanged(channel);
                }
                if (!record.isCanceled()) checkItems(tempChannel, record);
            } catch (EOFException e) {
            }
        }
    }
