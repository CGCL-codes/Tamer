    private void printMidiMessages(TreeMap<Long, ArrayList<ShortMessage>> msgMap) {
        for (Long tmpTimeStamp : msgMap.keySet()) {
            for (ShortMessage shortMsg : msgMap.get(tmpTimeStamp)) {
                System.out.print("TIME: " + tmpTimeStamp + " CHANNEL: " + shortMsg.getChannel());
                System.out.print(" COMMAND: " + shortMsg.getCommand() + " DATA1: " + shortMsg.getData1());
                System.out.println(" DATA2: " + shortMsg.getData2());
            }
        }
    }
