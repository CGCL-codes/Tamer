    public void sonify() {
        if (CHORDS) {
            for (int i = 0; i < NUMBALLS; i++) {
                int[] oldchord = balls[i].getCurrentChord();
                int[] chord = balls[i].getNewChord();
                boolean chordChanged = false;
                for (int c = 0; c < 3; c++) {
                    if ((!isTracking() || BALLVIEW == i) && chord[c] != oldchord[c]) {
                        if (HOLD_CHORD) JMIDI.getChannel(i).noteOff(oldchord[c]); else if (!chordChanged) {
                            JMIDI.getChannel(i).allNotesOff();
                        }
                        JMIDI.getChannel(i).noteOn(chord[c], MAX_VOL);
                        chordChanged = true;
                    }
                }
            }
        } else {
            for (int i = 0; i < NUMBALLS; i++) {
                if (collisionMat[i] == COLL_BUFF) {
                    int p = balls[i].getCurrentPitch();
                    JMIDI.getChannel(i).noteOn(p, MAX_VOL);
                    balls[i].setPitch(p);
                }
            }
            for (int i = 0; i < NUMBALLS; i++) {
                if (collisionMat[i] == 0) {
                    JMIDI.getChannel(i).noteOff(balls[i].getLastPitch());
                }
            }
        }
    }
