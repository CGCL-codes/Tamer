    public void setMidiChannel(String midiChannel2) {
        try {
            int midiChannel = Integer.parseInt(midiChannel2) - 1;
            if (midiChannel < 0 || midiChannel > 15) {
                this.gui.getChannelTF().setText("" + (this.midiChannel + 1));
                return;
            }
            this.midiChannel = midiChannel;
            this.gui.getChannelTF().setText(midiChannel2);
        } catch (NumberFormatException e) {
            this.gui.getChannelTF().setText("" + (this.midiChannel + 1));
            return;
        }
    }
