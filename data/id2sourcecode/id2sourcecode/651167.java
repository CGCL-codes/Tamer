    public Channel openChannel(String tag) {
        Channel channel = getChannel(tag);
        if (null != channel) {
            return channel;
        }
        channel = new Channel(tag);
        channels.addElement(channel);
        String[] p = { tag };
        sendMessage("mode", p);
        ChannelWindow cw = new ChannelWindow(this, channel, locale);
        properties.addObserver(cw);
        cw.update(properties, null);
        cw.setFont(getFont());
        cw.setForeground(mainfg);
        cw.setBackground(mainbg);
        cw.setTextForeground(textfg);
        cw.setTextBackground(textbg);
        cw.setSelectedForeground(selfg);
        cw.setSelectedBackground(selbg);
        channel_windows.put(tag, cw);
        chat_panel.add(cw, tag);
        cw.validate();
        chat_panel.show(tag);
        cw.requestFocus();
        cw.addChatPanelListener(this);
        return channel;
    }
