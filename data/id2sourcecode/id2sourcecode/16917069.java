    public void userJoined(User user, Channel channel) {
        if (addingUserIndex != 0) {
            delayedEvents.add(new EventInfo(channel, user, EventInfo.TYPE_JOIN));
            return;
        }
        channel = getChannel(channel);
        if (channel != null) {
            int addedIndex = channel.addUser(user);
            if (channel.equals(selectedChannel) && addedIndex != -1) insertUser(user, addedIndex);
        } else {
        }
    }
