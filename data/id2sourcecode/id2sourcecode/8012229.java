    private void populateTextFields() {
        String pageName = (String) getPageCB().getSelectedItem();
        if (pageName != null) {
            String[] pieces = pageName.split(":");
            pageIndex = Integer.parseInt(pieces[0]) - 1;
            getChannelTF().setText("" + (midiChannels[pageIndex] + 1));
            getNoteTF().setText("" + midiNotes[pageIndex]);
            getPageChangeDelayTF().setText("" + pageChangeDelays[pageIndex]);
            getMidiCCTF().setText("" + midiCCs[pageIndex]);
            getMidiCCValTF().setText("" + midiCCVals[pageIndex]);
            if (linkedDevices[pageIndex] != null) {
                int deviceIdx = 0;
                for (int i = 0; i < getLinkedDeviceCB().getItemCount(); i++) {
                    String itemName = getLinkedDeviceCB().getItemAt(i).toString();
                    if (itemName.compareTo(linkedDevices[pageIndex]) == 0) {
                        deviceIdx = i;
                    }
                }
                getLinkedDeviceCB().setSelectedIndex(deviceIdx);
                getLinkedPageCB().setSelectedIndex(linkedPages[pageIndex]);
            } else {
                getLinkedDeviceCB().setSelectedIndex(0);
            }
        }
    }
