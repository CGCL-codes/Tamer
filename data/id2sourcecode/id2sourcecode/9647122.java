        protected void implSend(MidiMessage message, long timeStamp) {
            if (recording) {
                long tickPos = 0;
                if (timeStamp < 0) {
                    tickPos = getTickPosition();
                } else {
                    synchronized (tempoCache) {
                        tickPos = MidiUtils.microsecond2tick(sequence, timeStamp, tempoCache);
                    }
                }
                Track track = null;
                if (message.getLength() > 1) {
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if ((sm.getStatus() & 0xF0) != 0xF0) {
                            track = RecordingTrack.get(recordingTracks, sm.getChannel());
                        }
                    } else {
                        track = RecordingTrack.get(recordingTracks, -1);
                    }
                    if (track != null) {
                        if (message instanceof ShortMessage) {
                            message = new FastShortMessage((ShortMessage) message);
                        } else {
                            message = (MidiMessage) message.clone();
                        }
                        MidiEvent me = new MidiEvent(message, tickPos);
                        track.add(me);
                    }
                }
            }
        }
