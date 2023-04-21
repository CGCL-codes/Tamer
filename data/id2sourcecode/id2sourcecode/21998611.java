    public static void printShortMessage(ShortMessage shortMsg) {
        System.out.print("\tShortMessage. CHANNEL: " + shortMsg.getChannel());
        System.out.print(" COMMAND: " + shortMsg.getCommand() + " DATA1: " + shortMsg.getData1());
        System.out.println(" DATA2: " + shortMsg.getData2());
    }
