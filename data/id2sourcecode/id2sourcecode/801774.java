        public void run() {
            if (sliceData != null) {
                int sliceThresholdInt = Math.round((float) sliceData.length * _sliceThreshold);
                if (sliceData != null) {
                    attacks = new ArrayList<Integer>();
                    boolean[] attacksArray = new boolean[sliceData.length];
                    int checkSpace = 1;
                    if (2 * sliceExclusionRadius > sliceData.length) {
                        post("MP7 Encoder Error: slice exclusion diameter greater than sample length\n");
                        return;
                    } else {
                        checkSpace = sliceExclusionRadius;
                    }
                    for (int j = 0; j < sliceThresholdInt; j++) {
                        int i = sliceData[j].getFrameNumber();
                        boolean alreadyHit = false;
                        if (i >= checkSpace && i <= sliceData.length - 1 - checkSpace) {
                            for (int k = checkSpace * -1; k <= checkSpace; k++) {
                                if (attacksArray[i + k]) {
                                    alreadyHit = true;
                                    break;
                                }
                            }
                            if (!alreadyHit) {
                                attacksArray[i] = true;
                            }
                        } else {
                            if (i - checkSpace < 0) {
                                for (int k = 0; k <= i + checkSpace; k++) {
                                    if (attacksArray[i + k]) {
                                        alreadyHit = true;
                                        break;
                                    }
                                }
                                if (!alreadyHit) {
                                    attacksArray[i] = true;
                                }
                            } else {
                                for (int k = -checkSpace; k + i < attacksArray.length - 1; k++) {
                                    if (attacksArray[i + k]) {
                                        alreadyHit = true;
                                        break;
                                    }
                                }
                                if (!alreadyHit) {
                                    attacksArray[i] = true;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < attacksArray.length; i++) {
                        if (attacksArray[i]) {
                            Integer a = new Integer(i);
                            attacks.add(a);
                        }
                    }
                    sliceThreshold = _sliceThreshold;
                    outlet(3, "clear");
                    if (attacks.size() > 0) {
                        float audioLength = 0f;
                        if (bufferOperation) {
                            if (bufferArgs.length > 0) {
                                String bufferName = bufferArgs[0].getString();
                                if (MSPBuffer.getChannels(bufferName) != 0) {
                                    if (MSPBuffer.getLength(bufferName) <= Float.MAX_VALUE) {
                                        audioLength = (float) MSPBuffer.getLength(bufferName);
                                    }
                                }
                            }
                        } else {
                            try {
                                ais = AudioSystem.getAudioInputStream(new File(fileName));
                                if (ais.getFrameLength() <= Integer.MAX_VALUE) {
                                    audioLength = 1000f * (float) ais.getFrameLength() / ais.getFormat().getFrameRate();
                                }
                            } catch (UnsupportedAudioFileException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (attacks.size() == 1) {
                            outlet(3, new Atom[] { Atom.newAtom(0), Atom.newAtom(hopSize * attacks.get(0)), Atom.newAtom(audioLength - hopSize * attacks.get(0)) });
                        } else {
                            for (int i = 0; i < attacks.size() - 1; i++) {
                                outlet(3, new Atom[] { Atom.newAtom(i), Atom.newAtom(hopSize * attacks.get(i)), Atom.newAtom(hopSize * attacks.get(i + 1) - hopSize * attacks.get(i)) });
                            }
                            outlet(3, new Atom[] { Atom.newAtom((attacks.size() - 1)), Atom.newAtom(hopSize * attacks.get(attacks.size() - 1)), Atom.newAtom(audioLength - hopSize * attacks.get(attacks.size() - 1)) });
                        }
                        outlet(2, attacks.size());
                        if (lcd instanceof MaxBox) {
                            lcd.send("clear", null);
                            for (int i = 0; i < attacks.size(); i++) {
                                int attack = attacks.get(i);
                                lcd.send("frgb", new Atom[] { Atom.newAtom(255), Atom.newAtom(0), Atom.newAtom(0) });
                                lcd.send("linesegment", new Atom[] { Atom.newAtom(((float) attack / (float) mpeg7.audioPower.length) * lcdWidth), Atom.newAtom(0), Atom.newAtom(((float) attack / (float) mpeg7.audioPower.length) * lcdWidth), Atom.newAtom(lcdHeight) });
                            }
                        }
                    } else {
                        outlet(2, 0);
                        if (lcd instanceof MaxBox) {
                            lcd.send("clear", null);
                        }
                    }
                } else {
                    return;
                }
            } else {
            }
        }
