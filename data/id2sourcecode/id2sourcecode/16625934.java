    private void populateRoom(MeetMeRoomImpl room) {
        final CommandAction meetMeListAction;
        final ManagerResponse response;
        final List<String> lines;
        final Collection<Integer> userNumbers = new ArrayList<Integer>();
        meetMeListAction = new CommandAction(MEETME_LIST_COMMAND + " " + room.getRoomNumber());
        try {
            response = server.sendAction(meetMeListAction);
        } catch (ManagerCommunicationException e) {
            logger.error("Unable to send \"" + MEETME_LIST_COMMAND + "\" command", e);
            return;
        }
        if (response instanceof ManagerError) {
            logger.error("Unable to send \"" + MEETME_LIST_COMMAND + "\" command: " + response.getMessage());
            return;
        }
        if (!(response instanceof CommandResponse)) {
            logger.error("Response to \"" + MEETME_LIST_COMMAND + "\" command is not a CommandResponse but " + response.getClass());
            return;
        }
        lines = ((CommandResponse) response).getResult();
        for (String line : lines) {
            final Matcher matcher;
            final Integer userNumber;
            final AsteriskChannelImpl channel;
            boolean muted = false;
            boolean talking = false;
            MeetMeUserImpl channelUser;
            MeetMeUserImpl roomUser;
            matcher = MEETME_LIST_PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }
            userNumber = Integer.valueOf(matcher.group(1));
            channel = channelManager.getChannelImplByName(matcher.group(2));
            userNumbers.add(userNumber);
            if (line.contains("(Admin Muted)") || line.contains("(Muted)")) {
                muted = true;
            }
            if (line.contains("(talking)")) {
                talking = true;
            }
            channelUser = channel.getMeetMeUser();
            if (channelUser != null && channelUser.getRoom() != room) {
                channelUser.left(DateUtil.getDate());
                channelUser = null;
            }
            roomUser = room.getUser(userNumber);
            if (roomUser != null && roomUser.getChannel() != channel) {
                room.removeUser(roomUser);
                roomUser = null;
            }
            if (channelUser == null && roomUser == null) {
                final MeetMeUserImpl user;
                user = new MeetMeUserImpl(server, room, userNumber, channel, DateUtil.getDate());
                user.setMuted(muted);
                user.setTalking(talking);
                room.addUser(user);
                channel.setMeetMeUserImpl(user);
                server.fireNewMeetMeUser(user);
            } else if (channelUser != null && roomUser == null) {
                channelUser.setMuted(muted);
                room.addUser(channelUser);
            } else if (channelUser == null) {
                roomUser.setMuted(muted);
                channel.setMeetMeUserImpl(roomUser);
            } else {
                if (channelUser != roomUser) {
                    logger.error("Inconsistent state: channelUser != roomUser, channelUser=" + channelUser + ", roomUser=" + roomUser);
                }
            }
        }
        Collection<MeetMeUserImpl> users = room.getUserImpls();
        Collection<MeetMeUserImpl> usersToRemove = new ArrayList<MeetMeUserImpl>();
        for (MeetMeUserImpl user : users) {
            if (!userNumbers.contains(user.getUserNumber())) {
                usersToRemove.add(user);
            }
        }
        for (MeetMeUserImpl user : usersToRemove) {
            user.left(DateUtil.getDate());
            room.removeUser(user);
            user.getChannel().setMeetMeUserImpl(null);
        }
    }
