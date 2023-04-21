    public void userLeft(User user, Channel channel) {
        if (addingUserIndex != 0) {
            delayedEvents.add(new EventInfo(channel, user, EventInfo.TYPE_LEFT));
            return;
        }
        channel = getChannel(channel);
        if (channel != null) {
            channel.removeUser(user);
            if (channel.equals(selectedChannel)) removeUser(user);
        } else {
        }
    }
