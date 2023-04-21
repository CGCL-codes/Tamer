    public synchronized void checkSensor() {
        int note;
        saveSensorInfo(previousIndex);
        saveModuleInfo(previousModuleIndex);
        if (lf != null) {
            lf.checkSensor();
        }
        values[previousIndex] = (int) inputLevel.getMaximumColoredValue();
        for (int i = 0; i < 14; i++) {
            if ((i < 8 || i > 12) && si[i].isActive()) {
                if (rangeMin[i] <= values[i] && rangeMax[i] >= values[i]) {
                    if (!trigger[i]) {
                        System.out.println("Trigger! " + rangeMin[i] + " < " + values[i] + " < " + rangeMax[i]);
                        note = getActiveNote(i);
                        if (note > 0) {
                            mo.sendMidi(new byte[] { (byte) (144 + getChannel(i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                            delayMidi(new byte[] { (byte) (128 + getChannel(i)), (byte) note, (byte) 0 }, i);
                        }
                        trigger[i] = true;
                    }
                } else {
                    if (trigger[i]) {
                        if (release[i]) {
                            System.out.println("Release! " + rangeMin[i] + " < " + values[i] + " < " + rangeMax[i]);
                            note = getActiveNote(i);
                            if (note > 0) {
                                mo.sendMidi(new byte[] { (byte) (144 + getChannel(i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                                delayMidi(new byte[] { (byte) (128 + getChannel(i)), (byte) note, (byte) 0 }, i);
                            }
                        }
                        trigger[i] = false;
                    }
                }
                for (int j = 0; i != 8 && j < 3; j++) {
                    if (rangeModMin[i][j] <= values[i] && rangeModMax[i][j] >= values[i] && !si[i].isTrigger()) {
                        module[i][j] = true;
                        note = getActiveNote(i, j);
                        if (note > 0) {
                            if (si[i].getCi().getMi()[j].isPitch()) {
                                if (tls[i][j] != null) {
                                    note = (int) (16384 * tls[i][j].getEase());
                                }
                                if (si[i].getCi().getMi()[j].isInvert()) {
                                    note = 16384 - note;
                                }
                                System.out.println("Pitch = " + note);
                                mo.sendMidi(new byte[] { (byte) (224 + getChannel(i, j)), (byte) (note % 0x7F), (byte) (note / 128) }, de.humatic.mmj.MidiSystem.getHostTime());
                            } else if (si[i].getCi().getMi()[j].isVel()) {
                                int velocity;
                                velocity = getVelocity(i, j);
                                if (tls[i][j] != null) {
                                    velocity = (int) (127 * tls[i][j].getEase());
                                }
                                if (si[i].getCi().getMi()[j].isInvert()) {
                                    velocity = 127 - velocity;
                                }
                                System.out.println("Velocity = " + getVelocity(i, j));
                                mo.sendMidi(new byte[] { (byte) (144 + getChannel(i, j)), (byte) note, (byte) velocity }, de.humatic.mmj.MidiSystem.getHostTime());
                            } else if (si[i].getCi().getMi()[j].isMod()) {
                                if (tls[i][j] != null) {
                                    note = (int) (127 * tls[i][j].getEase());
                                }
                                if (si[i].getCi().getMi()[j].isInvert()) {
                                    note = 127 - note;
                                }
                                mo.sendMidi(new byte[] { (byte) (176 + getChannel(i, j)), (byte) 1, (byte) (note) }, de.humatic.mmj.MidiSystem.getHostTime());
                            }
                        }
                    } else if (module[i][j] == true && (rangeModMin[i][j] >= values[i] || rangeModMax[i][j] < values[i]) && !si[i].isTrigger()) {
                        int value;
                        if (values[i] < rangeModMin[i][j]) {
                            value = 0;
                        } else {
                            value = 1;
                        }
                        if (si[i].getCi().getMi()[j].isPitch()) {
                            note = 16384 * value;
                            System.out.println("Pitch = " + note);
                            if (si[i].getCi().getMi()[j].isInvert()) {
                                note = 16384 - note;
                            }
                            mo.sendMidi(new byte[] { (byte) (224 + getChannel(i, j)), (byte) (note % 0x7F), (byte) (note / 128) }, de.humatic.mmj.MidiSystem.getHostTime());
                        } else if (si[i].getCi().getMi()[j].isVel()) {
                            int velocity;
                            velocity = 127 * value;
                            note = getActiveNote(i, j);
                            if (si[i].getCi().getMi()[j].isInvert()) {
                                velocity = 127 - velocity;
                            }
                            mo.sendMidi(new byte[] { (byte) (144 + getChannel(i, j)), (byte) note, (byte) velocity }, de.humatic.mmj.MidiSystem.getHostTime());
                        } else if (si[i].getCi().getMi()[j].isMod()) {
                            note = 127 * value;
                            if (si[i].getCi().getMi()[j].isInvert()) {
                                note = 127 - note;
                            }
                            mo.sendMidi(new byte[] { (byte) (176 + getChannel(i, j)), (byte) 1, (byte) (note) }, de.humatic.mmj.MidiSystem.getHostTime());
                        }
                    }
                    module[i][j] = false;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            if (!names[i].equals("")) {
                if (tags.contains(names[i])) {
                    if (!trigger[8 + i]) {
                        if (!lastTag[0].equals(names[i])) {
                            note = getActiveNote(8 + i);
                            if (note > 0) {
                                mo.sendMidi(new byte[] { (byte) (144 + getChannel(8 + i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                                delayMidi(new byte[] { (byte) (128 + getChannel(8 + i)), (byte) note, (byte) 0 }, 8 + i);
                            }
                        } else {
                            if (si[8 + i].isInvert()) {
                                note = getActiveNote(8 + i);
                                if (note > 0) {
                                    mo.sendMidi(new byte[] { (byte) (144 + getChannel(8 + i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                                    delayMidi(new byte[] { (byte) (128 + getChannel(8 + i)), (byte) note, (byte) 0 }, 8 + i);
                                }
                            }
                        }
                        lastTag[0] = names[i];
                        trigger[8 + i] = true;
                    }
                } else {
                    if (trigger[8 + i]) {
                        if (release[8 + i]) {
                            if (!lastReleaseTag[0].equals(names[i])) {
                                note = getActiveNote(8 + i);
                                if (note > 0) {
                                    mo.sendMidi(new byte[] { (byte) (144 + getChannel(8 + i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                                    delayMidi(new byte[] { (byte) (128 + getChannel(8 + i)), (byte) note, (byte) 0 }, 8 + i);
                                }
                            } else {
                                if (si[8 + i].isInvert()) {
                                    note = getActiveNote(8 + i);
                                    if (note > 0) {
                                        mo.sendMidi(new byte[] { (byte) (144 + getChannel(8 + i)), (byte) note, (byte) 127 }, de.humatic.mmj.MidiSystem.getHostTime());
                                        delayMidi(new byte[] { (byte) (128 + getChannel(8 + i)), (byte) note, (byte) 0 }, 8 + i);
                                    }
                                }
                            }
                        }
                        lastReleaseTag[0] = names[i];
                        trigger[8 + i] = false;
                    }
                }
            }
        }
    }
