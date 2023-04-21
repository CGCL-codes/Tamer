    void handleMeetMeEvent(AbstractMeetMeEvent event) {
        String roomNumber;
        Integer userNumber;
        AsteriskChannelImpl channel;
        MeetMeRoomImpl room;
        MeetMeUserImpl user;
        roomNumber = event.getMeetMe();
        if (roomNumber == null) {
            logger.warn("RoomNumber (meetMe property) is null. Ignoring " + event.getClass().getName());
            return;
        }
        userNumber = event.getUserNum();
        if (userNumber == null) {
            logger.warn("UserNumber (userNum property) is null. Ignoring " + event.getClass().getName());
            return;
        }
        user = getOrCreateUserImpl(event);
        if (user == null) {
            return;
        }
        channel = user.getChannel();
        room = user.getRoom();
        if (event instanceof MeetMeLeaveEvent) {
            logger.info("Removing channel " + channel.getName() + " from room " + roomNumber);
            if (room != user.getRoom()) {
                if (user.getRoom() != null) {
                    logger.error("Channel " + channel.getName() + " should be removed from room " + roomNumber + " but is user of room " + user.getRoom().getRoomNumber());
                    user.getRoom().removeUser(user);
                } else {
                    logger.error("Channel " + channel.getName() + " should be removed from room " + roomNumber + " but is user of no room");
                }
            }
            user.left(event.getDateReceived());
            room.removeUser(user);
            channel.setMeetMeUserImpl(null);
        } else if (event instanceof MeetMeTalkingEvent) {
            Boolean status;
            status = ((MeetMeTalkingEvent) event).getStatus();
            if (status != null) {
                user.setTalking(status);
            } else {
                user.setTalking(true);
            }
        } else if (event instanceof MeetMeMuteEvent) {
            Boolean status;
            status = ((MeetMeMuteEvent) event).getStatus();
            if (status != null) {
                user.setMuted(status);
            }
        }
    }
