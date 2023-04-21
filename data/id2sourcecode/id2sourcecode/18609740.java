    public void removeChannel(Channel channel) {
        for (int i = 0; i < contentPanel.getWidgetCount(); i++) if (((ChannelLine) contentPanel.getWidget(i)).getChannel().equals(channel)) {
            contentPanel.remove(i);
            break;
        }
    }
