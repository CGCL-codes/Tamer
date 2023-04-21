    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelDialog c = ChannelDialog.getChannelDialog();
        String channel = c.getName();
        if (channel == null || channel.length() <= 0) {
            c.setStatusText(getString(c, "NO_NAME"));
            return;
        }
        c.setFrozen(true);
        EnterChannelMessage message = new EnterChannelMessage();
        message.channel = channel;
        Window.getWindow().channel = channel;
        try {
            SocketClient.getInstance().send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
