    public void startup() throws ManagerCommunicationException {
        asteriskServer.addAsteriskServerListener(this);
        for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
        }
        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
            for (AsteriskChannel asteriskChannel : asteriskQueue.getEntries()) {
            }
        }
        for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
            System.out.println(meetMeRoom);
            for (MeetMeUser user : meetMeRoom.getUsers()) {
                user.addPropertyChangeListener(this);
            }
        }
    }
