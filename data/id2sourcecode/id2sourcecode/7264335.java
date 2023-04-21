    public void setMidiSequence(Sequence midiSequence) {
        this.timeBase = midiSequence.getResolution();
        Track midiTracks[] = midiSequence.getTracks();
        for (int t = 0; t < midiTracks.length; t++) {
            for (int i = 0; i < midiTracks[t].size(); i++) {
                javax.sound.midi.MidiMessage midiMessage = midiTracks[t].get(i).getMessage();
                if (midiMessage instanceof MetaMessage && ((MetaMessage) midiMessage).getType() == MidiConstants.META_TEMPO) {
                    MetaMessage metaMessage = (MetaMessage) midiMessage;
                    byte data[] = metaMessage.getData();
                    int timeBase = TempoMessage.getNearestTimeBase(this.timeBase);
                    int l = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | data[2] & 0xff;
                    int tempo = (int) Math.round(60d * 1000000d / ((48d / timeBase) * l));
                    for (int divider = (tempo + 254) / 255; tempo > 255; divider *= 2) {
                        timeBase = TempoMessage.getNearestTimeBase(timeBase / divider);
                        tempo = (int) Math.round(60d * 1000000d / ((48d / timeBase) * l));
                        double scale = (double) this.timeBase / timeBase;
                        if (scale > this.scale) {
                            this.scale = scale;
                            scaleChanged = true;
                        }
                    }
                }
            }
        }
        this.scale = Math.ceil(this.scale);
        Debug.println("(SCALE) final scale: " + scale + ", " + scaleChanged);
        for (int t = 0; t < midiTracks.length; t++) {
            for (int i = 0; i < midiTracks[t].size(); i++) {
                this.midiEvents.add(midiTracks[t].get(i));
            }
        }
        Collections.sort(midiEvents, new Comparator<MidiEvent>() {

            /** */
            public int compare(MidiEvent e1, MidiEvent e2) {
                long t1 = e1.getTick();
                long t2 = e2.getTick();
                if (t1 - t2 != 0) {
                    return (int) (t1 - t2);
                } else {
                    int c1 = getChannel(e1);
                    int c2 = getChannel(e2);
                    return c1 - c2;
                }
            }

            /** */
            int getChannel(MidiEvent e) {
                MidiMessage m = e.getMessage();
                if (m instanceof ShortMessage) {
                    return ((ShortMessage) m).getChannel();
                } else {
                    return -1;
                }
            }
        });
        this.noteOffEventUsed = new BitSet(midiEvents.size());
    }
