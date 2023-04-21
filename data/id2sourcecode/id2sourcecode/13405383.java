    public void midiMessage(MidiMessage message) {
        if (seq.isRunning()) {
            long tick = seq.getTickPosition();
            if (message instanceof ShortMessage) {
                ShortMessage shm = (ShortMessage) message;
                System.out.println(tick + " : " + seq.getLoopCount() + "   " + shm);
                switch(shm.getCommand()) {
                    case ShortMessage.NOTE_ON:
                        System.out.println(" NOTE_ON:" + shm.getData1() + "  " + shm.getData2());
                        break;
                    case ShortMessage.NOTE_OFF:
                        System.out.println("NOTE_OFF:" + shm.getData1() + "  " + shm.getData2());
                        break;
                    case ShortMessage.CONTROL_CHANGE:
                        System.out.println("    CNTRL:" + shm.getData1() + "  " + shm.getData2());
                    default:
                        System.out.println("    ????:" + shm.getData1() + "  " + shm.getData2());
                        break;
                }
                if (shm.getCommand() == ShortMessage.NOTE_ON || shm.getCommand() == ShortMessage.NOTE_OFF) {
                    if (shm.getCommand() == ShortMessage.NOTE_OFF || shm.getData2() == 0) {
                        NoteEvent noteEvent = pendingNoteEvents.get(shm.getChannel() << 8 | shm.getData1());
                        if (noteEvent != null) {
                            noteEvent.setDuration(tick - noteEvent.getStartTick());
                            pendingNoteEvents.remove(shm.getChannel() << 8 | shm.getData1());
                            addEventToRecordingTracks(noteEvent);
                        }
                    } else {
                        pendingNoteEvents.put(shm.getChannel() << 8 | shm.getData1(), new NoteEvent((FrinikaTrackWrapper) null, tick, shm.getData1(), shm.getData2(), shm.getChannel(), 0));
                    }
                }
            }
        }
    }
