    protected static void createRoom(Player thisPlayer, String[] information) {
        Channel currentChannel = ChannelData.getChannel(information[0]);
        Room[] ghostrooms = currentChannel.getGhostRooms(thisPlayer);
        if (ghostrooms != null) {
            for (Room g : ghostrooms) {
                g.close();
            }
        }
        if (thisPlayer.getCurrentRoom() != null) {
            thisPlayer.getCurrentRoom().close();
        }
        thisPlayer.setNotReady();
        String name = information[1];
        String password = information[2];
        int limit = Room.LIMIT;
        try {
            limit = Integer.parseInt(information[3]);
        } catch (Exception e) {
        }
        boolean instantLaunch = Boolean.parseBoolean(information[4]);
        boolean doSearch = Boolean.parseBoolean(information[7]);
        Map<String, String> interfaceIps = Protocol.decodeIPMap(information[5]);
        String modindex = information[6];
        Room newroom = new Room(name, thisPlayer, password, limit, currentChannel, instantLaunch, interfaceIps, doSearch);
        newroom.setModIndex(modindex);
        currentChannel.addRoom(newroom);
        if (!instantLaunch) {
            Protocol.createRoom(thisPlayer.getConnection(), currentChannel, newroom);
            if (thisPlayer.isPlaying()) {
                Protocol.sendRoomPlayingStatus(thisPlayer.getConnection(), thisPlayer);
            }
        } else {
            Protocol.sendChannelPlayingStatus(currentChannel, thisPlayer);
        }
        thisPlayer.setCurrentRoom(newroom);
    }
