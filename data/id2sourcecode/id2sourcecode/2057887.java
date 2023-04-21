    protected vavi.sound.mfi.Sequence convert(Sequence midiSequence, int fileType) throws InvalidMfiDataException, IOException {
        Debug.println("divisionType: " + midiSequence.getDivisionType());
        Debug.println("microsecondLength: " + midiSequence.getMicrosecondLength());
        Debug.println("resolution: " + midiSequence.getResolution());
        Debug.println("tickLength: " + midiSequence.getTickLength());
        vavi.sound.mfi.Sequence mfiSequence = new vavi.sound.mfi.Sequence();
        MfiContext mfiContext = new MfiContext();
        mfiContext.setType(fileType);
        mfiContext.setMidiSequence(midiSequence);
        for (int i = 0; i < mfiContext.getSequenceSize(); i++) {
            MidiEvent midiEvent = mfiContext.getMidiEvent(i);
            MidiMessage midiMessage = midiEvent.getMessage();
            Track[] mfiTracks = mfiSequence.getTracks();
            int maxTracks = mfiTracks.length;
            String key = null;
            int mfiTrackNumber = 0;
            if (midiMessage instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) midiMessage;
                int channel = shortMessage.getChannel();
                int command = shortMessage.getCommand();
                int data1 = shortMessage.getData1();
                mfiTrackNumber = mfiContext.retrieveMfiTrack(channel);
                if (!mfiContext.isTrackUsed(mfiTrackNumber) && maxTracks <= mfiTrackNumber) {
                    for (int j = maxTracks; j <= mfiTrackNumber; j++) {
                        mfiSequence.createTrack();
                        Debug.println(">>>> create MFi track: " + j);
                        mfiTracks = mfiSequence.getTracks();
                        maxTracks = mfiTracks.length;
                    }
                }
                mfiContext.setTrackUsed(mfiTrackNumber, true);
                if ((command & 0xf0) == 0xb0) {
                    key = "midi.short." + (command & 0xf0) + "." + data1;
                } else {
                    key = "midi.short." + (command & 0xf0);
                }
            } else if (midiMessage instanceof SysexMessage) {
                SysexMessage sysexMessage = (SysexMessage) midiMessage;
                byte[] data = sysexMessage.getData();
                if (maxTracks == 0) {
                    mfiSequence.createTrack();
                    Debug.println("create MFi track: 0");
                    mfiTracks = mfiSequence.getTracks();
                    maxTracks = mfiTracks.length;
                }
                mfiContext.setTrackUsed(0, true);
                mfiTrackNumber = 0;
                key = "midi.sysex." + data[0];
            } else if (midiMessage instanceof MetaMessage) {
                MetaMessage metaMessage = (MetaMessage) midiMessage;
                int meta = metaMessage.getType();
                if (maxTracks == 0) {
                    mfiSequence.createTrack();
                    Debug.println("create MFi track: 0");
                    mfiTracks = mfiSequence.getTracks();
                    maxTracks = mfiTracks.length;
                }
                mfiContext.setTrackUsed(0, true);
                mfiTrackNumber = 0;
                key = "midi.meta." + meta;
            }
            MfiConvertible converter = MfiConvertibleFactory.getConverter(key);
            if (converter instanceof EndOfTrackMessage) {
                MfiEvent[] mfiEvents = converter.getMfiEvents(midiEvent, mfiContext);
                for (int t = 0; t < mfiEvents.length && t < maxTracks; t++) {
                    if (mfiEvents[t] != null) {
                        if (!mfiContext.isEofSet(t)) {
                            MfiEvent[] nops = mfiContext.getIntervalMfiEvents(t);
                            if (nops != null) {
                                for (int j = 0; j < nops.length; j++) {
                                    mfiTracks[t].add(nops[j]);
                                }
                            }
                            mfiTracks[t].add(mfiEvents[t]);
                            mfiContext.setEofSet(t, true);
                        } else {
                        }
                    } else {
                        Debug.println("message is null[" + mfiTracks[t].size() + "]: " + midiMessage);
                    }
                }
            } else if (converter != null) {
                MfiEvent[] mfiEvents = mfiContext.getIntervalMfiEvents(mfiTrackNumber);
                if (mfiEvents != null) {
                    for (int j = 0; j < mfiEvents.length; j++) {
                        if (mfiEvents[j] == null) {
                            Debug.println(Level.WARNING, "NOP is null[" + mfiTracks[mfiTrackNumber].size() + "]: " + MidiUtil.paramString(midiMessage));
                        }
                        addEventToTrack(mfiContext, midiEvent.getTick(), mfiTracks[mfiTrackNumber], mfiTrackNumber, mfiEvents[j]);
                    }
                }
                mfiEvents = converter.getMfiEvents(midiEvent, mfiContext);
                if (mfiEvents != null) {
                    for (int j = 0; j < mfiEvents.length; j++) {
                        if (mfiEvents[j] == null) {
                            Debug.println(Level.WARNING, "event is null[" + mfiTracks[mfiTrackNumber].size() + ", " + mfiEvents.length + "]: " + converter.getClass() + ", " + MidiUtil.paramString(midiMessage));
                        }
                        addEventToTrack(mfiContext, midiEvent.getTick(), mfiTracks[mfiTrackNumber], mfiTrackNumber, mfiEvents[j]);
                    }
                }
            } else {
            }
        }
        return mfiSequence;
    }
