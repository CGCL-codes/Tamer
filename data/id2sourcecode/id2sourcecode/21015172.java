            public VisibleTrackItem(int trk) {
                super(getSequence().getMidiTrack(trk).getTrackName());
                track = getSequence().getMidiTrack(trk);
                setState(isVisibleTrack(track));
                MidiTrack track = getSequence().getMidiTrack(trk);
                if (track.getChannel() >= 0) {
                    setBackground(MidiColor.asHSB((Float) track.getClientProperty("Hue"), 0.342f, 1.0f));
                    addActionListener(this);
                }
            }
