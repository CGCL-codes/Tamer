    public void userJoined(String nick, Channel chan) {
        final User selectedUser = getUser(nick);
        if (selectedUser == null) {
            this.getUserList().add(addUser(nick, chan));
        } else {
            if (!selectedUser.getChannels().containsKey(chan)) {
                selectedUser.getChannels().put(chan, UserChannelPermission.STANDARD);
            }
        }
        chan.getUserList().add(selectedUser);
    }
