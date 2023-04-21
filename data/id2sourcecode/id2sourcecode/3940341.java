    private static void getNotesFromSequenceInSequencer() {
        Integer channel, midiNoteNumber, velocity;
        Long onset, duration = 0l;
        MidiEvent event, event2;
        MidiMessage message, message2;
        ShortMessage msg, msg2;
        try {
            sequencer.open();
            Track[] tracks = sequencer.getSequence().getTracks();
            int nrTracks = tracks.length;
            for (Integer trackNr = 0; trackNr < nrTracks; trackNr++) {
                int n = tracks[trackNr].size();
                for (int i = 0; i < n; i++) {
                    event = tracks[trackNr].get(i);
                    message = event.getMessage();
                    if (message instanceof ShortMessage) {
                        msg = (ShortMessage) message;
                        channel = msg.getChannel();
                        if (msg.getCommand() == ShortMessage.NOTE_ON) {
                            midiNoteNumber = msg.getData1();
                            velocity = msg.getData2();
                            onset = event.getTick();
                            for (int j = i + 1; j < n; j++) {
                                event2 = tracks[trackNr].get(j);
                                message2 = event2.getMessage();
                                if (message2 instanceof ShortMessage) {
                                    msg2 = (ShortMessage) message2;
                                    if (msg2.getChannel() == channel && msg2.getData1() == midiNoteNumber && (msg2.getCommand() == ShortMessage.NOTE_OFF || (msg2.getCommand() == ShortMessage.NOTE_ON && msg2.getData2() == 0))) {
                                        duration = event2.getTick() - onset;
                                        break;
                                    }
                                }
                            }
                            notes.add(new MidiNote(onset, midiNoteNumber, velocity, duration, channel, trackNr));
                        }
                    }
                }
            }
            sequencer.close();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
