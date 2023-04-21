    public void controllerSelected(int index) {
        curController = index;
        RSController controller = getCurrentController();
        for (int i = 0; i < joystickPanels.length; i++) {
            if (i >= controller.getChannelCount()) {
                joystickPanels[i].setVisible(false);
            } else {
                joystickPanels[i].setVisible(true);
                joystickPanels[i].setReversed(controller.isReversed(i));
            }
        }
        for (int i = 0; i < heliPanels.length; i++) {
            int channel = controller.getAxisForControl(heliPanels[i].getControl());
            if (channel >= 0) {
                heliPanels[i].setMappedChannel(channel);
            }
        }
    }
