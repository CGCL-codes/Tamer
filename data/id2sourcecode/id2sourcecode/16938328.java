    private void parseTrack(ArrayList<Note> noteList, final Track track, final TreeMap<Long, Float> tempos) {
        TempoTickInfo tempoTickInfo = new TempoTickInfo();
        tempoTickInfo.tick = 0;
        tempoTickInfo.iterator = tempos.entrySet().iterator();
        if (isPPQ) {
            tempoTickInfo.next = tempoTickInfo.iterator.next();
            tempoTickInfo.previous = new AbstractMap.SimpleImmutableEntry<Long, Float>(0l, MidiSequencer.TICKS_PER_MINUTE_FOR_PPQ / (120 * ticksPerSecondBase));
        }
        final MidiEvent[] noteOns = new MidiEvent[128];
        final int max = track.size();
        for (int i = 0; i < max; ++i) {
            MidiEvent midiEvent = track.get(i);
            final MidiMessage message = midiEvent.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
                channels[shortMessage.getChannel()] = true;
                final int command = shortMessage.getCommand();
                boolean noteOff = false;
                if (command == ShortMessage.NOTE_ON) {
                    if (shortMessage.getData2() == 0) {
                        noteOff = true;
                    } else {
                        if (isPPQ) {
                            tempoTickInfo = shiftTempoTickInfo(midiEvent.getTick(), tempoTickInfo);
                            midiEvent = new MidiEvent(midiEvent.getMessage(), tempoTickInfo.tick + (long) ((midiEvent.getTick() - tempoTickInfo.previous.getKey()) * tempoTickInfo.previous.getValue()));
                        }
                        noteOns[shortMessage.getData1()] = midiEvent;
                    }
                } else if (command == ShortMessage.NOTE_OFF) {
                    noteOff = true;
                }
                if (noteOff) {
                    final int midiKey = shortMessage.getData1();
                    final MidiEvent onNote = noteOns[midiKey];
                    if (onNote != null) {
                        noteOns[midiKey] = null;
                        long length;
                        if (isPPQ) {
                            tempoTickInfo = shiftTempoTickInfo(midiEvent.getTick(), tempoTickInfo);
                            length = tempoTickInfo.tick + (long) ((midiEvent.getTick() - tempoTickInfo.previous.getKey()) * tempoTickInfo.previous.getValue()) - onNote.getTick();
                        } else {
                            length = midiEvent.getTick() - onNote.getTick();
                        }
                        noteList.add(new Note(new Key(midiKey, ((ShortMessage) onNote.getMessage()).getData2()), onNote.getTick(), length));
                    }
                }
            }
        }
    }
