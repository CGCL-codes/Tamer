    public void commandGETCHANNEL(Integer tunerNumber) {
        int channel = controller.getChannel(tunerNumber);
        System.out.println("channel " + channel);
    }
