    private void storeValues() {
        try {
            int value = Integer.parseInt(getChannelTF().getText());
            midiChannels[pageIndex] = value - 1;
            value = Integer.parseInt(getNoteTF().getText());
            midiNotes[pageIndex] = value;
            value = Integer.parseInt(getPageChangeDelayTF().getText());
            pageChangeDelays[pageIndex] = value;
            value = Integer.parseInt(getMidiCCTF().getText());
            midiCCs[pageIndex] = value;
            value = Integer.parseInt(getMidiCCValTF().getText());
            midiCCVals[pageIndex] = value;
            String device = linkedDeviceCB.getSelectedItem().toString();
            if (device.compareTo("-- Select Device --") != 0) {
                linkedDevices[pageIndex] = device;
                int pageNum = linkedPageCB.getSelectedIndex();
                linkedPages[pageIndex] = pageNum;
            } else {
                linkedDevices[pageIndex] = null;
                linkedPages[pageIndex] = 0;
            }
        } catch (NumberFormatException ex) {
        }
    }
