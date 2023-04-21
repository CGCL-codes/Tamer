    public Room createRoom(String roomName, boolean isQuery) {
        ircController = getIRCProtocol().getLocalClient();
        Room bmroom;
        if (isQuery) {
            bmroom = ircController.onJoinedQuery(ircController.getCreateUser(roomName));
        } else {
            bmroom = ircController.onMeJoinedChannel(roomName, ircController.getCreateUser(getActiveNick())).getRoom();
        }
        reportStatus(("Joined to") + " " + roomName);
        String roomName_low = roomName.toLowerCase();
        addRoomPanel(roomName_low, bmroom);
        if (!isQuery) {
            IRCChannel chan = (IRCChannel) bmroom;
            chan.joins(ircController.getChannelRoleByNickName(chan, getActiveNick()));
        }
        return bmroom;
    }
