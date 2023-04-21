    public void run() throws ManagerCommunicationException {
        for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
            System.out.println(asteriskChannel);
        }
        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
            System.out.println(asteriskQueue);
        }
        for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
            System.out.println(meetMeRoom);
        }
    }
