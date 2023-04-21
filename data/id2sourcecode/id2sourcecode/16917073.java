    public void userModeChange(Channel channel, User user, char mode, boolean adding) {
        if (addingUserIndex != 0) {
            delayedEvents.add(new EventInfo(adding, channel, mode, null, user, EventInfo.TYPE_MODE));
            return;
        }
        channel = getChannel(channel);
        if (channel != null) {
            User thisUser = channel.getUser(user);
            if (adding) thisUser.addMode(mode); else thisUser.removeMode(mode);
            Vector users = (Vector) channel.getUsers();
            users.remove(thisUser);
            int index = channel.addUser(thisUser);
            if (channel.equals(selectedChannel)) {
                removeUser(thisUser);
                insertUser(thisUser, index);
            }
        } else {
        }
    }
