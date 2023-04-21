    private void parseTracks(final int[] trackNums, final TreeMap<Long, Float> tempos) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        final Track[] tracks = midiSequence.getTracks();
        for (int i = 0; i < channels.length; ++i) {
            channels[i] = false;
        }
        for (int trackNum : trackNums) {
            parseTrack(noteList, tracks[trackNum], tempos);
        }
        for (int trackNum = 0; trackNum < tracks.length; ++trackNum) {
            if (Arrays.binarySearch(trackNums, trackNum) >= 0) continue;
            Track track = tracks[trackNum];
            final int max = track.size();
            for (int i = 0; i < max; ++i) {
                final MidiMessage message = track.get(i).getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage shortMessage = (ShortMessage) message;
                    channels[shortMessage.getChannel()] = true;
                }
            }
        }
        Collections.sort(noteList);
        noteSequence = noteList.toArray(Game.State.NULL_NOTES);
    }
