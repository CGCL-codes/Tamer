        public void send(MidiMessage message, long timeStamp) {
            long tick = player.getRealTimeTickPosition();
            for (RecordableLane rlane : recordingLanes) {
                if (!(rlane instanceof MidiLane)) continue;
                MidiLane lane = (MidiLane) rlane;
                MidiDevice dev = lane.getMidiDevice();
                MidiPlayOptions po = lane.getPlayOptions();
                if (po.muted) continue;
                if (dev == null) continue;
                if (message instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) message;
                    try {
                        ShortMessage throughShm;
                        int cmd = shm.getCommand();
                        int chn = lane.getMidiChannel();
                        int dat1 = shm.getData1();
                        int dat2 = shm.getData2();
                        throughShm = new ShortMessage();
                        if ((cmd == ShortMessage.NOTE_ON) || (cmd == ShortMessage.NOTE_OFF)) {
                            dat1 = FrinikaSequencerPlayer.applyPlayOptionsNote(po, dat1);
                            dat2 = FrinikaSequencerPlayer.applyPlayOptionsVelocity(po, dat2);
                            throughShm.setMessage(cmd, chn, dat1, dat2);
                            dev.getReceiver().send(throughShm, -1);
                        } else {
                            throughShm.setMessage(cmd, chn, dat1, dat2);
                            dev.getReceiver().send(throughShm, -1);
                            System.out.println(shm.getData1() + " " + shm.getData2() + "  " + throughShm.getData1() + " " + throughShm.getData2());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (true) return;
            if (recording) {
                if (player.getLoopCount() > lastLoopCount) {
                    newRecordingTake();
                    lastLoopCount = player.getLoopCount();
                }
                if (message instanceof ShortMessage) {
                    ShortMessage shm = (ShortMessage) message;
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
                    } else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                        addEventToRecordingTracks(new ControllerEvent((FrinikaTrackWrapper) null, tick, shm.getData1(), shm.getData2()));
                    } else if (shm.getCommand() == ShortMessage.PITCH_BEND) {
                        addEventToRecordingTracks(new PitchBendEvent((FrinikaTrackWrapper) null, tick, ((shm.getData1()) | (shm.getData2() << 7)) & 0x7fff));
                    }
                }
            }
        }
