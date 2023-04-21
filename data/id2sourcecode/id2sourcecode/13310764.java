    public void showLink(Object channelsList) {
        try {
            Object node = getSelectedItem(channelsList);
            if (node != null) {
                long id = ((Long) getProperty(node, "id")).longValue();
                ChannelIF channel = DAOChannel.getChannel(id);
                showLink(channel.getSite() == null ? "" : channel.getSite().toExternalForm());
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
