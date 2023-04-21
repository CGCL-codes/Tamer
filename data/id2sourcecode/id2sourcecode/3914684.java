    public boolean authenticate(String username, String password) {
        user = ((BGOApplication) getApplication()).getDATA().loginUser(username, password, this.getId());
        this.messageCount = Math.max(ChatChannel.getChannel(null).getMessages().size() - 10, 0);
        if (user != null) {
            ChatChannel.getChannel(null).postMessage(user.getName() + " has joined the Lobby");
            return true;
        }
        return false;
    }
