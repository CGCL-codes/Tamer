    public void startSenderInitialisation() {
        System.out.println("startSenderInitialisationThread = " + InitialisationBS.writeStart());
        sendBroadcastMessage(InitialisationBS.writeStart());
        Utils.sleep(10);
    }
