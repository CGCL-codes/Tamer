    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add MIDI Output")) {
            String[] midiOutOptions = this.monome.getMidiOutOptions();
            String deviceName = (String) JOptionPane.showInputDialog(this.monome, "Choose a MIDI Output to add", "Add MIDI Output", JOptionPane.PLAIN_MESSAGE, null, midiOutOptions, "");
            if (deviceName == null) {
                return;
            }
            this.addMidiOutDevice(deviceName);
        }
        if (e.getActionCommand().equals("Update Preferences")) {
            this.delayAmount = Integer.parseInt(this.getDelayTF().getText());
            this.midiChannel = Integer.parseInt(this.getChannelTF().getText()) - 1;
            if (this.midiChannel < 0) this.midiChannel = 0;
            this.ccOffset = Integer.parseInt(this.getCcOffsetTF().getText());
        }
    }
