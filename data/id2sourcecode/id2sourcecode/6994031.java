    public void setTrackVolume(int track, int volume) throws NoChannelAssignedException {
        int channel = getTrackChannel(track);
        Track t = getTrack(track);
        Vector<MidiEvent> events = ProjectHelper.filterControlChanges(t, ControlChangeNumber.CHANNEL_VOLUME_MSB);
        ShortMessage m;
        if (events.size() > 0) {
            for (MidiEvent e : events) {
                m = (ShortMessage) e.getMessage();
                try {
                    m.setMessage(m.getCommand(), m.getChannel(), m.getData1(), volume);
                } catch (InvalidMidiDataException ex) {
                }
            }
        } else {
            m = new ShortMessage();
            try {
                m.setMessage(ShortMessage.CONTROL_CHANGE, channel, ControlChangeNumber.CHANNEL_VOLUME_MSB, volume);
            } catch (InvalidMidiDataException ex) {
            }
            t.add(new MidiEvent(m, 0));
        }
        setChannelVolume(channel, volume);
        reloadSequence();
        this.notifyObservers(Action.TRACK_PARAMETERS_EDITED);
    }
