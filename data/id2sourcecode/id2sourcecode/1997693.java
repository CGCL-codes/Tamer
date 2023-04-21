    public void userParted(String nick, Channel chan) {
        User selectedUser = getUser(nick);
        if (selectedUser != null) {
            if (selectedUser.getChannels().containsKey(chan)) {
                selectedUser.getChannels().remove(chan);
            }
            chan.getUserList().remove(selectedUser);
            if (selectedUser.getChannels().isEmpty()) {
                this.getUserList().remove(selectedUser);
                selectedUser = null;
            }
        }
    }
