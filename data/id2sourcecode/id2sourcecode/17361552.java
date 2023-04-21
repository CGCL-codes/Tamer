    public SmafEvent[] getIntervalSmafEvents() {
        int interval = 0;
        int track = 0;
        MidiEvent midiEvent = midiTrack.get(midiEventIndex);
        MidiMessage midiMessage = midiEvent.getMessage();
        if (midiMessage instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) midiMessage;
            int channel = shortMessage.getChannel();
            track = retrieveSmafTrack(channel);
            interval = retrieveDelta(track, midiEvent.getTick());
        } else if (midiMessage instanceof MetaMessage && ((MetaMessage) midiMessage).getType() == 81) {
            track = smafTrackNumber;
            interval = retrieveDelta(track, midiEvent.getTick());
            Debug.println("interval for tempo[" + smafTrackNumber + "]: " + interval);
        } else if (midiMessage instanceof MetaMessage && ((MetaMessage) midiMessage).getType() == 47) {
            track = smafTrackNumber;
            interval = retrieveDelta(track, midiEvent.getTick());
            Debug.println("interval for EOT[" + smafTrackNumber + "]: " + interval);
        } else if (midiMessage instanceof SysexMessage) {
            return null;
        } else {
            Debug.println(Level.WARNING, "not supported message: " + midiMessage);
            return null;
        }
        if (interval < 0) {
            Debug.println(Level.WARNING, "interval: " + interval);
            interval = 0;
        }
        int nopLength = interval / 255;
        if (nopLength == 0) {
            return null;
        }
        SmafEvent[] smafEvents = new SmafEvent[nopLength];
        for (int i = 0; i < nopLength; i++) {
            NopMessage smafMessage = new NopMessage(255);
            smafEvents[i] = new SmafEvent(smafMessage, 0l);
            incrementBeforeTick(track, 255);
        }
        ;
        return smafEvents;
    }
