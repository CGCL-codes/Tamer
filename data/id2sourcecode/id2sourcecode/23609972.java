    private void reconcileSubChannelRemoveInBlueArrangement(String removedChannel) {
        BlueData data = BlueSystem.getCurrentBlueData();
        if (data == null) {
            return;
        }
        Arrangement arr = data.getArrangement();
        for (int i = 0; i < arr.size(); i++) {
            Instrument instr = arr.getInstrument(i);
            if (instr instanceof BlueSynthBuilder) {
                BlueSynthBuilder bsb = (BlueSynthBuilder) instr;
                BSBGraphicInterface bsbInterface = bsb.getGraphicInterface();
                for (int j = 0; j < bsbInterface.size(); j++) {
                    BSBObject bsbObj = bsbInterface.getBSBObject(j);
                    if (bsbObj instanceof BSBSubChannelDropdown) {
                        BSBSubChannelDropdown bsbSubDrop = (BSBSubChannelDropdown) bsbObj;
                        if (bsbSubDrop.getChannelOutput().equals(removedChannel)) {
                            bsbSubDrop.setChannelOutput(Channel.MASTER);
                        }
                    }
                }
            }
        }
    }
