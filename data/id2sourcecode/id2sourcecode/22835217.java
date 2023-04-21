        private void setup() {
            for (int t = 0; t < getSequence().getMidiTrackCount(); t++) {
                MidiTrack track = getSequence().getMidiTrack(t);
                if (track.getChannel() < 0 && t > 0) continue;
                add(new TrackButton(track));
            }
        }
