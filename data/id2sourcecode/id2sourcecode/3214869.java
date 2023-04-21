    public void channelSourceChanged(final ChannelHistogram model, final ChannelSource channelSource) {
        final JTextField channelField = (JTextField) WINDOW_REFERENCE.getView("ChannelField");
        if (channelSource != null) {
            final Channel channel = channelSource.getChannel();
            if (channel != null) {
                channelField.setText(channel.getId());
                setRunIndicator(true);
            } else {
                channelField.setText("");
                setRunIndicator(false);
            }
        } else {
            channelField.setText("");
            setRunIndicator(false);
        }
        setHasChanges(true);
    }
