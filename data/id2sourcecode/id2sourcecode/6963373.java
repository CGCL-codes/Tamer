    private void handleHear(Command c) {
        KAHear hear = (KAHear) c;
        int toID = hear.getToID();
        int fromID = hear.getFromID();
        int length = hear.getLength();
        byte[] msg = hear.getData();
        byte channel = hear.getChannel();
        hear(fromID, msg, channel);
    }
