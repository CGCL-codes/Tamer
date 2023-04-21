    public void removeMidiInputListener(MidiNoteListener m) {
        for (int i = 0; i < mnolCount; i++) {
            if (mnol[i] == m) {
                mnol[i] = mnol[i + 1];
                m = mnol[i];
            }
        }
        milCount--;
    }
