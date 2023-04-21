    public void moveUserToChannel(String user, String channel) {
        removeUser(user);
        Channel c = getChannel(channel);
        c.users.add(user);
        fireContentsChanged(this, 0, getSize());
    }
