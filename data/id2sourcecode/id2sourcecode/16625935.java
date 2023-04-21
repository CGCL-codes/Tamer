    private MeetMeUserImpl getOrCreateUserImpl(AbstractMeetMeEvent event) {
        final String roomNumber;
        final MeetMeRoomImpl room;
        final String uniqueId;
        final AsteriskChannelImpl channel;
        MeetMeUserImpl user;
        roomNumber = event.getMeetMe();
        room = getOrCreateRoomImpl(roomNumber);
        user = room.getUser(event.getUserNum());
        if (user != null) {
            return user;
        }
        uniqueId = event.getUniqueId();
        if (uniqueId == null) {
            logger.warn("UniqueId is null. Ignoring MeetMeEvent");
            return null;
        }
        channel = channelManager.getChannelImplById(uniqueId);
        if (channel == null) {
            logger.warn("No channel with unique id " + uniqueId + ". Ignoring MeetMeEvent");
            return null;
        }
        user = channel.getMeetMeUser();
        if (user != null) {
            logger.error("Got MeetMeEvent for channel " + channel.getName() + " that is already user of a room");
            user.left(event.getDateReceived());
            if (user.getRoom() != null) {
                user.getRoom().removeUser(user);
            }
            channel.setMeetMeUserImpl(null);
        }
        logger.info("Adding channel " + channel.getName() + " as user " + event.getUserNum() + " to room " + roomNumber);
        user = new MeetMeUserImpl(server, room, event.getUserNum(), channel, event.getDateReceived());
        room.addUser(user);
        channel.setMeetMeUserImpl(user);
        server.fireNewMeetMeUser(user);
        return user;
    }
