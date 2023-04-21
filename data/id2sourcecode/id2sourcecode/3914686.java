    public boolean logout() {
        ChatChannel.getChannel(null).postMessage(user.getName() + " has left the Lobby");
        DataProvider.getInstance().getPlayerList().remove(this.getId());
        user = null;
        return user == null;
    }
