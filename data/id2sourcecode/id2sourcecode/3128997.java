    public void updateController() {
        RSController controller = getCurrentController();
        for (int i = 0; i < controller.getChannelCount(); i++) {
            joystickPanels[i].setValue(controller.getChannelValue(i, calibrate.isSelected()));
        }
        for (HeliControlPanel heliPanel : heliPanels) {
            heliPanel.setValue(controller.getControlValue(heliPanel.getControl()));
        }
        txView.updateControls();
        txView.repaint();
    }
