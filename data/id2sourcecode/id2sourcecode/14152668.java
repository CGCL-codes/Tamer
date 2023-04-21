    public BlmAgent(AcceleratorSeq aSequence, BLM newBlmNode) {
        blmNode = newBlmNode;
        sequence = aSequence;
        lossavgch = blmNode.getChannel(BLM.LOSS_AVG_HANDLE);
        makeLossChannelConnectionListener();
        lossavgch.requestConnection();
        lossavgch.pendIO(5);
    }
