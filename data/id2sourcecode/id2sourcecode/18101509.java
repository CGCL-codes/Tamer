    public void ExtractNotes() {
        if (tracks != null && tracks.length > 0) {
            for (int i = 0; i < tracks.length; i++) {
                for (int j = 0; j < tracks[i].size(); j++) {
                    javax.sound.midi.MidiMessage e = tracks[i].get(j).getMessage();
                    if (e instanceof javax.sound.midi.ShortMessage) {
                        javax.sound.midi.ShortMessage m = ((javax.sound.midi.ShortMessage) e);
                        if ((m.getCommand() == javax.sound.midi.ShortMessage.NOTE_ON) && m.getData2() != 0) {
                            int chan = m.getChannel();
                            int notenum = m.getData1();
                            javax.sound.midi.MidiEvent notenende = null;
                            int k = j + 1;
                            while ((notenende == null) && k < tracks[i].size()) {
                                javax.sound.midi.MidiMessage e2 = tracks[i].get(k).getMessage();
                                if (e2 instanceof javax.sound.midi.ShortMessage) {
                                    javax.sound.midi.ShortMessage m2 = ((javax.sound.midi.ShortMessage) e2);
                                    if (((m2.getCommand() == javax.sound.midi.ShortMessage.NOTE_OFF) || ((m2.getCommand() == javax.sound.midi.ShortMessage.NOTE_ON))) && ((m2.getChannel() == chan) && (m2.getData1() == notenum))) {
                                        notenende = tracks[i].get(k);
                                    }
                                }
                                k++;
                            }
                            if (notenende != null) {
                                notes.add(new EditorNote(tracks[i].get(j), notenende, tracks[i]));
                            }
                        }
                    }
                }
            }
        }
    }
