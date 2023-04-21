    @SuppressWarnings("null")
    private void checkMessage(ShortMessage message, int device, boolean noteOn) {
        boolean noteOff = false;
        if (noteOn) {
            if (message.getData2() == 0) {
                noteOff = true;
            } else {
                Game.activateKey(message, device);
                if (Game.isGameActive()) {
                    PressedNote[][] noteOns = deviceNoteOns.get(device);
                    if (noteOns == null) {
                        noteOns = new PressedNote[16][128];
                        deviceNoteOns.put(device, noteOns);
                    }
                    noteOns[message.getChannel()][message.getData1()] = new PressedNote(getTickPosition(), message.getData2());
                }
            }
        } else {
            noteOff = true;
        }
        if (noteOff) {
            Game.deactivateKey(message, device);
            if (Game.isGameActive()) {
                PressedNote[][] noteOns = deviceNoteOns.get(device);
                if (noteOns == null) return;
                final int midiKey = message.getData1();
                final int channel = message.getChannel();
                final PressedNote onNote = noteOns[channel][midiKey];
                if (onNote != null) {
                    if (midiKey < Graphs.getFirstKeyInMidi() || midiKey > Graphs.getLastKeyInMidi()) {
                        noteOns[channel][midiKey] = null;
                        return;
                    }
                    if (onNote.startTime == eventTickTime || eventTickTime == -1) {
                        if (Util.getDebugLevel() > 30) Util.debug("updating eventTickTime");
                        eventTickTime = Long.MAX_VALUE;
                        for (byte i = 0; i < noteOns.length; ++i) {
                            if (noteOns[i] != null) for (int j = 0; j < noteOns[i].length; ++j) {
                                if (noteOns[i][j] != null && noteOns[i][j].startTime < eventTickTime) {
                                    eventTickTime = noteOns[i][j].startTime;
                                }
                            }
                        }
                        final long limit = eventTickTime - delay;
                        while (eventTickPosition < notes.length && notes[eventTickPosition].time < limit) {
                            if (!notes[eventTickPosition].checked) {
                                notes[eventTickPosition].checked = true;
                            }
                            ++eventTickPosition;
                        }
                        if (eventTickTime == onNote.startTime) eventTickTime = -1;
                    }
                    noteOns[channel][midiKey] = null;
                    onNote.alter(getTickPosition(), 0);
                    ArrayList<Note> foundNotes = new ArrayList<Note>();
                    long endTime;
                    final Key key = new Key(midiKey, 0);
                    for (int i = eventTickPosition; i < notes.length; ++i) {
                        if (notes[i].time - delay <= onNote.startTime) {
                            if (key.equals(notes[i].key) && !notes[i].checked && (onNote.startTime <= notes[i].time + delay) && ((endTime = notes[i].time + notes[i].length) - delay <= onNote.lastTime && onNote.lastTime <= endTime + delay)) {
                                foundNotes.add(notes[i]);
                            }
                        } else {
                            break;
                        }
                    }
                    if (foundNotes.size() == 0) {
                        Game.noteScored(key, -25);
                    } else {
                        int maxScore = Integer.MIN_VALUE;
                        Note selected = null;
                        for (Note note : foundNotes) {
                            int score = calculateScore(onNote, note);
                            if (score > maxScore) {
                                maxScore = score;
                                selected = note;
                            }
                        }
                        selected.score = maxScore;
                        selected.checkedTime = onNote.lastTime;
                        selected.checked = true;
                        Game.noteScored(key, maxScore);
                    }
                }
            }
        }
    }
