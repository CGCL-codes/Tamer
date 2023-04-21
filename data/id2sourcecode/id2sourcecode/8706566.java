    private ProjectContainer(ProjectSettings project) throws Exception {
        defaultInit();
        System.out.println(" LOADING PROJECT ");
        ByteArrayInputStream sequenceInputStream = new ByteArrayInputStream(project.getSequence());
        sequencer = new FrinikaSequencer();
        sequencer.open();
        attachTootNotifications();
        createSequencerPriorityListener();
        renderer = new FrinikaRenderer(this);
        sequence = new FrinikaSequence(MidiSequenceConverter.splitChannelsToMultiTrack(MidiSystem.getSequence(sequenceInputStream)));
        sequencer.setSequence(sequence);
        tempoList = new TempoList(sequence.getResolution(), this);
        tempoList.add(0, tempo);
        sequencer.setTempoList(tempoList);
        try {
            setTempoInBPM(MidiSequenceConverter.findFirstTempo(sequence));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        SynthRack synthRack = new SynthRack(null);
        SynthWrapper midiDev = new SynthWrapper(this, synthRack);
        synthRack.loadSynthSetup(project.getSynthSettings());
        Vector<FrinikaTrackWrapper> origTracks = sequence.getFrinikaTrackWrappers();
        Vector<FrinikaTrackWrapper> tracks = new Vector<FrinikaTrackWrapper>(origTracks);
        projectLane = new ProjectLane(this);
        origTracks.removeAllElements();
        for (FrinikaTrackWrapper ftw : tracks) {
            MidiMessage msg = ftw.get(0).getMessage();
            if (msg instanceof ShortMessage) {
                ftw.setMidiDevice(midiDev);
                ftw.setMidiChannel(((ShortMessage) msg).getChannel());
                System.out.println(((ShortMessage) msg).getChannel() + " channel");
            }
            MidiLane lane = new MidiLane(ftw, this);
            if (ftw.getMidiChannel() > -1) {
                lane.setProgram(ftw.getMidiChannel(), 0, 0);
            }
            projectLane.addChildLane(lane);
            MidiPart part = new MidiPart(lane);
            long startTick = 0;
            long endTick = Long.MAX_VALUE;
            part.importFromMidiTrack(startTick, endTick);
        }
        int ticks = (int) getSequence().getTickLength();
        endTick = Math.max(endTick, ticks);
        addMidiOutDevice(midiDev);
        postInit();
        rebuildGUI();
    }
