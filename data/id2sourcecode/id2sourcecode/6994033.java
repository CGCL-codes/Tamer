    public void setTrackBalance(int track, int balance) throws NoChannelAssignedException {
        Track t = getTrack(track);
        int channel = getTrackChannel(track);
        Vector<MidiEvent> events = ProjectHelper.filterControlChanges(t, ControlChangeNumber.CHANNEL_BALANCE_MSB);
        ShortMessage m;
        if (events.size() > 0) {
            for (MidiEvent e : events) {
                m = (ShortMessage) e.getMessage();
                try {
                    m.setMessage(m.getCommand(), m.getChannel(), m.getData1(), balance);
                } catch (InvalidMidiDataException ex) {
                }
            }
        } else {
            m = new ShortMessage();
            try {
                m.setMessage(ShortMessage.CONTROL_CHANGE, channel, ControlChangeNumber.CHANNEL_BALANCE_MSB, balance);
            } catch (InvalidMidiDataException ex) {
            }
            t.add(new MidiEvent(m, 0));
        }
        MidiChannel ch = synthesizer.getChannels()[channel];
        ch.controlChange(ControlChangeNumber.CHANNEL_BALANCE_MSB, balance);
        reloadSequence();
        this.notifyObservers(Action.TRACK_PARAMETERS_EDITED);
    }
