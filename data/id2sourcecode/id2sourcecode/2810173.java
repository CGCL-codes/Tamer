    public void run() throws ManagerCommunicationException {
        asteriskServer.addAsteriskServerListener(this);
        for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
            System.out.println(asteriskChannel);
            asteriskChannel.addPropertyChangeListener(this);
        }
        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
            System.out.println(asteriskQueue);
            for (AsteriskChannel asteriskChannel : asteriskQueue.getEntries()) {
                asteriskChannel.addPropertyChangeListener(this);
            }
        }
        for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
            System.out.println(meetMeRoom);
            for (MeetMeUser user : meetMeRoom.getUsers()) {
                user.addPropertyChangeListener(this);
            }
        }
    }
