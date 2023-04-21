    public Sequence storeSequence(String name) {
        Sequence snapshot;
        try {
            snapshot = new Sequence(Sequence.PPQ, 1);
        } catch (InvalidMidiDataException imde) {
            return null;
        }
        int providerId = -1;
        int moduleId = -1;
        int instanceIndex = -1;
        for (int synth = 0; synth < rackControls.size(); synth++) {
            SynthControls synthControls = rackControls.getSynthControls(synth);
            Track t = snapshot.createTrack();
            if (synthControls == null) continue;
            try {
                MidiMessage msg = createMeta(TRACK_NAME, synthControls.getName());
                t.add(new MidiEvent(msg, 0L));
                msg = off(0, synthControls.getProviderId(), synthControls.getId());
                t.add(new MidiEvent(msg, 0L));
            } catch (InvalidMidiDataException imde) {
                System.err.println("Synth store: failed to store synth " + synthControls.getName());
            }
            NativeSupport ns = synthControls.getNativeSupport();
            if (ns != null && ns.canPersistMidi()) {
                ns.store(t);
            } else if (synthControls instanceof ChannelledSynthControls) {
                ChannelledSynthControls channelledControls = (ChannelledSynthControls) synthControls;
                CompoundControl gc = channelledControls.getGlobalControls();
                if (gc != null) {
                    providerId = gc.getProviderId();
                    moduleId = gc.getId();
                    instanceIndex = 0;
                    MidiPersistence.store(providerId, moduleId, instanceIndex, gc, t);
                }
                SynthChannelControls cc;
                for (int chan = 0; chan < 16; chan++) {
                    cc = channelledControls.getChannelControls(chan);
                    if (cc == null) continue;
                    providerId = cc.getProviderId();
                    moduleId = cc.getId();
                    instanceIndex = 1 + chan;
                    MidiPersistence.store(providerId, moduleId, instanceIndex, cc, t);
                    for (int i = 0; i < 128; i++) {
                        try {
                            int cid = cc.getMappedControlId(i);
                            if (cid < 0) continue;
                            MidiMessage msg = on(chan, i, cid);
                            t.add(new MidiEvent(msg, 0L));
                        } catch (InvalidMidiDataException imde) {
                            System.err.println("Synth store: failed on mapping controller" + i);
                        }
                    }
                }
            } else {
                providerId = synthControls.getProviderId();
                moduleId = synthControls.getId();
                instanceIndex = 0;
                MidiPersistence.store(providerId, moduleId, instanceIndex, synthControls, t);
            }
        }
        return snapshot;
    }
