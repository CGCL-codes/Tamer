    public void setMidiChannel(String midiChannel2) {
        this.midiChannel = Integer.parseInt(midiChannel2) - 1;
        this.getChannelTF().setText(midiChannel2);
    }
