            public TrackItem(MidiTrack track) {
                super(track.getTrackName());
                this.track = track;
                setSelected(track == getSelectedTrack());
                if (track.getChannel() >= 0) {
                    setBackground(MidiColor.asHSB((Float) track.getClientProperty("Hue"), 0.342f, 1.0f));
                }
                addActionListener(this);
            }
