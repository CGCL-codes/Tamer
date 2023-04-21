    public void updateInfo() {
        channelCB.getModel().setSelectedItem("#" + control.getChannel());
        deviceCB.setSelectedIndex(MidiManager.getMidiDevices().indexOf(control.getDevice()) + 1);
        controlSpinner.setValue(control.getControl());
        colorPanel.setColor(control.getColor());
        baseColorPanel.setColor(control.getBaseColor());
        String[] newSweeperList = new String[(ColourControlsMain.getGUI().getImageGUIs().size())];
        int index = 0;
        for (ImageGUI imageGUI : ColourControlsMain.getGUI().getImageGUIs()) {
            newSweeperList[index++] = imageGUI.getSweeper().getName();
            MyLogger.debug("List: " + imageGUI.getSweeper().getName());
        }
        sweeperList.setListData(newSweeperList);
        if (control.getSweeper() != null) {
            sweeperList.setSelectedValue(control.getSweeper().getName(), true);
        }
        updateControlValue();
        repaint();
    }
