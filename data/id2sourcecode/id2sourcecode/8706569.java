    public ProjectContainer(Sequence seq, MidiDevice midiDevice, boolean adjustPPQ) throws Exception {
        defaultInit();
        System.out.println(" LOADING MIDI SEQUENCE ");
        sequencer = new FrinikaSequencer();
        sequencer.open();
        attachTootNotifications();
        createSequencerPriorityListener();
        renderer = new FrinikaRenderer(this);
        if (seq.getDivisionType() == Sequence.PPQ) {
            ticksPerQuarterNote = seq.getResolution();
            System.out.println(" Ticks per quater is " + ticksPerQuarterNote);
        } else {
            System.out.println("WARNING: The resolution type of the imported Sequence is not supported by Frinika");
        }
        FrinikaSequence seq1 = new FrinikaSequence(MidiSequenceConverter.splitChannelsToMultiTrack(seq));
        if (adjustPPQ) {
            try {
                throw new Exception(" adjust PPQ not implemented yet");
            } catch (Exception ex) {
                Logger.getLogger(ProjectContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sequence = seq1;
        }
        sequencer.setSequence(sequence);
        Vector<FrinikaTrackWrapper> origTracks = sequence.getFrinikaTrackWrappers();
        Vector<FrinikaTrackWrapper> tracks = new Vector<FrinikaTrackWrapper>(origTracks);
        projectLane = new ProjectLane(this);
        int n = origTracks.size();
        for (int i = n - 1; i > 0; i--) {
            origTracks.remove(i);
        }
        for (FrinikaTrackWrapper ftw : tracks) {
            for (int i = 0; i < ftw.size(); i++) {
                MidiMessage msg = ftw.get(i).getMessage();
                if (msg instanceof ShortMessage) {
                    ftw.setMidiChannel(((ShortMessage) msg).getChannel());
                    System.out.println(((ShortMessage) msg).getChannel() + " channel");
                    break;
                }
            }
            int progChange = -1;
            for (int i = 0; i < ftw.size(); i++) {
                MidiMessage msg = ftw.get(i).getMessage();
                if (msg instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) msg;
                    if (shm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                        if (progChange == -1) {
                            String tt = MidiDebugDevice.eventToString(msg);
                            progChange = ((ShortMessage) msg).getData1();
                            System.out.println(" PROG CHANGE =" + tt + "    " + progChange);
                        } else {
                            System.out.println(" MULTIPLE PROG CHANGES !!!!!!");
                        }
                    }
                }
            }
            if (progChange >= 0) {
                MidiLane lane = new MidiLane(ftw, this);
                System.out.println(" Creting MidiLane with track " + count);
                lane.setProgram(progChange, 0, 0);
                projectLane.addChildLane(lane);
                MidiPart part = new MidiPart(lane);
                long startTick = 0;
                long endTick1 = Long.MAX_VALUE;
                part.importFromMidiTrack(startTick, endTick1);
                for (int i = 0; i < ftw.size(); i++) {
                    MidiEvent event = ftw.get(i);
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
            } else if (title == null) {
                for (int i = 0; i < ftw.size(); i++) {
                    MidiEvent event = ftw.get(i);
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
                                    title = txt;
                                    System.out.println("setTing title \"" + txt + "\"");
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            count = count + 1;
        }
        try {
            setTempoInBPM(MidiSequenceConverter.findFirstTempo(seq));
        } catch (Exception e) {
            e.printStackTrace();
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
        for (FrinikaTrackWrapper ftw : tracks) {
            if (midiDevice != null) {
                ftw.setMidiDevice(midiDevice);
            }
        }
        postInit();
    }
