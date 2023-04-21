    public void addUser(User u) {
        if (u.getChannel() != null) u.getChannel().removeUser(u);
        u.setChannel(this);
        for (User ou : users) try {
            ServerThread.broadcast(u, "adduser " + ou.getUserName(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.add(u);
        channelcast("adduser " + u.getUserName(), 0);
        channelcast(u.getUserName() + " has joined the channel.", 2);
    }
