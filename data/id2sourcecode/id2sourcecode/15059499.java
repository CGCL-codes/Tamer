    public static void printShortMessage(ShortMessage shortMessage) {
        System.out.print("ShortMessage:");
        System.out.print(" Command: " + shortMessage.getCommand());
        System.out.print(" Channel: " + shortMessage.getChannel());
        System.out.print(" Status: " + shortMessage.getStatus());
        System.out.print(" Data1: " + shortMessage.getData1());
        System.out.print(" Data2: " + shortMessage.getData2());
    }
