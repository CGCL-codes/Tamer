    public void chatPanelClosing(ChatPanelEvent ev) {
        ChatPanel source = ev.getChatPanel();
        String name = source.getPanelTag();
        chat_panel.remove(name);
        if (source instanceof PrivateWindow) {
            properties.deleteObserver((PrivateWindow) source);
            privates.remove(name);
        } else if (source instanceof ChannelWindow) {
            properties.deleteObserver((ChannelWindow) source);
            channels.removeElement(getChannel(name));
            channel_windows.remove(name);
        }
    }
