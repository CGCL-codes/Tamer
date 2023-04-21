    public void importFromMidiTrack(Track track, long startTickArg, long endTickArg) {
        HashMap<Integer, NoteEvent> pendingNoteEvents = new HashMap<Integer, NoteEvent>();
        for (int n = 0; n < track.size(); n++) {
            MidiEvent event = track.get(n);
            try {
                if (event.getMessage() instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) event.getMessage();
                    if (shm.getCommand() == ShortMessage.NOTE_ON || shm.getCommand() == ShortMessage.NOTE_OFF) {
                        if (shm.getCommand() == ShortMessage.NOTE_OFF || shm.getData2() == 0) {
                            NoteEvent noteEvent = pendingNoteEvents.get(shm.getChannel() << 8 | shm.getData1());
                            if (noteEvent == null) {
                                System.err.println("NoteOff event without start event, PLEASE FIX ME  in MidiPart ");
                                continue;
                            }
                            noteEvent.setEndEvent(event);
                            pendingNoteEvents.remove(shm.getChannel() << 8 | shm.getData1());
                            multiEvents.add(noteEvent);
                        } else {
                            if (event.getTick() >= startTickArg && event.getTick() < endTickArg) {
                                pendingNoteEvents.put(shm.getChannel() << 8 | shm.getData1(), new NoteEvent(this, event));
                            }
                        }
                    } else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                        if (event.getTick() >= startTickArg && event.getTick() < endTickArg) multiEvents.add(new ControllerEvent(this, event.getTick(), shm.getData1(), shm.getData2()));
                    } else if (shm.getCommand() == ShortMessage.PITCH_BEND) {
                        if (event.getTick() >= startTickArg && event.getTick() < endTickArg) multiEvents.add(new PitchBendEvent(this, event.getTick(), ((shm.getData1()) | (shm.getData2() << 7)) & 0x7fff));
                    }
                    if (event.getTick() >= endTickArg && pendingNoteEvents.size() == 0) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pendingNoteEvents.size() != 0) {
            System.err.println(" Some notes did not have a noteoff event ");
        }
        for (MultiEvent e : multiEvents) {
            e.zombie = false;
            if (e instanceof NoteEvent) {
                ((NoteEvent) e).validate();
            }
        }
        rebuildMultiEventEndTickComparables();
        setBoundsFromEvents();
    }
