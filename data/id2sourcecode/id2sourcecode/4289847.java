    private void update() {
        boolean login = getStore().getID() != null;
        boolean inChannel = getStore().getChannel() != null;
        String name = "";
        if (login) {
            name = " - " + getStore().getAuth().getUserName();
        }
        String online;
        online = login ? Strings.window_online : Strings.window_offline;
        String channel = "";
        if (inChannel) {
            String channelName = getStore().getChannel().getName();
            channel = String.format(Strings.window_channel, channelName);
        }
        String fmt = Strings.window_title + " %s %s %s";
        String title = String.format(fmt, online, name, channel);
        shell.setText(title);
        miLogin.setEnabled(!login);
        miLogout.setEnabled(login);
        miChannels.setEnabled(login && !inChannel);
        miLeaveChannel.setEnabled(login && inChannel);
    }
