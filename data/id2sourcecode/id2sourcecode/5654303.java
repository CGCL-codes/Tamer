    private void buildChannelSelector() {
        final ChannelGroup group = getSelectedChannelGroup();
        if (group != null && !AVAILABLE_SIGNALS.isEmpty()) {
            final List<String> signals = new ArrayList<String>(AVAILABLE_SIGNALS);
            final Collection<Channel> groupChannels = group.getChannels();
            for (final Channel channel : groupChannels) {
                signals.remove(channel.channelName());
            }
            _channelSelector = KeyValueRecordSelector.getInstanceWithFilterPrompt(signals, mainWindow, "Add Selected Channels", "Channel Filter", "toString");
            _channelSelector.getRecordTableModel().setColumnName("toString", "Channel");
        } else {
            _channelSelector = null;
        }
    }
