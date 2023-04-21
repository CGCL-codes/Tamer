    public BPMAgent(AcceleratorSeq aSequence, BPM newBPMNode, int numTurns) {
        xTBT = new double[numTurns];
        yTBT = new double[numTurns];
        xStats = new RunningWeightedStatistics(0.2);
        yStats = new RunningWeightedStatistics(0.2);
        BPMNode = newBPMNode;
        sequence = aSequence;
        BPMXChannel = BPMNode.getChannel(BPM.X_TBT_HANDLE);
        BPMYChannel = BPMNode.getChannel(BPM.Y_TBT_HANDLE);
        makeXChannelConnectionListener();
        makeYChannelConnectionListener();
        BPMXChannel.requestConnection();
        BPMYChannel.requestConnection();
        Channel.flushIO();
    }
