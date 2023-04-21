    public CalcLinacTiming(int ind, String name, BPMPane bpmPane) {
        bpmInd = ind;
        bpmName = name;
        thePane = bpmPane;
        bpmAvgStartCh = caF.getChannel(bpmName + ":avgStart_Rb");
        bpmAvgStartSetCh = caF.getChannel(bpmName + ":avgStart");
        bpmTDelayCh = caF.getChannel(bpmName + ":Delay00_Rb");
        bpmTDelaySetCh = caF.getChannel(bpmName + ":Delay00");
        bpmAvgStopCh = caF.getChannel(bpmName + ":avgStop_Rb");
        bpmAvgStopSetCh = caF.getChannel(bpmName + ":avgStop");
        bpmChopFreqCh = caF.getChannel(bpmName + ":chopFreq_Rb");
        bpmChopFreqSetCh = caF.getChannel(bpmName + ":chopFreq");
        bpmTDelay00Ch = caF.getChannel(bpmName + ":TurnsDelay00_Rb");
        bpmTDelay00SetCh = caF.getChannel(bpmName + ":TurnsDelay00");
    }
