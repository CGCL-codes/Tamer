    public void createMidiLanesFromSequence(Sequence seq, MidiDevice midiDevice) {
        if (seq.getDivisionType() == Sequence.PPQ) {
            int ticksPerQuarterNote1 = seq.getResolution();
            System.out.println(" Project PPQ = " + ticksPerQuarterNote);
            System.out.println(" Midi    PPQ = " + ticksPerQuarterNote1);
        } else {
            System.out.println("WARNING: The resolution type of the imported Sequence is not supported by Frinika");
        }
        Sequence splitSeq = MidiSequenceConverter.splitChannelsToMultiTrack(seq);
        int nTrack = splitSeq.getTracks().length;
        System.out.println(" Adding " + (nTrack) + " tracks ");
        getEditHistoryContainer().mark(getMessage("sequencer.project.add_midi_lane"));
        for (int iTrack = 0; iTrack < nTrack; iTrack++) {
            int chan = 0;
            Track track = splitSeq.getTracks()[iTrack];
            for (int i = 0; i < track.size(); i++) {
                MidiMessage msg = track.get(i).getMessage();
                if (msg instanceof ShortMessage) {
                    chan = ((ShortMessage) msg).getChannel();
                    break;
                }
            }
            MidiLane lane = createMidiLane();
            lane.setMidiChannel(chan);
            MidiPart part = new MidiPart(lane);
            long startTick = 0;
            long endTick = Long.MAX_VALUE;
            part.importFromMidiTrack(track, startTick, endTick);
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                if (event.getTick() > 0) {
                    break;
                }
                MidiMessage msg = event.getMessage();
                if (msg instanceof MetaMessage) {
                    MetaMessage meta = (MetaMessage) msg;
                    if (meta.getType() == 3) {
                        if (meta.getLength() > 0) {
                            try {
                                String txt = new String(meta.getData());
                                lane.setName(txt);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                }
            }
            part.commitEventsAdd();
        }
        rebuildGUI();
        if (midiDevice != null) {
            try {
                midiDevice = new SynthWrapper(this, midiDevice);
                addMidiOutDevice(midiDevice);
            } catch (Exception e2) {
                e2.printStackTrace();
                midiDevice = null;
            }
        }
        getEditHistoryContainer().notifyEditHistoryListeners();
    }
