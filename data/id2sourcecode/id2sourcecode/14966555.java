    public void removeMidiInputListener(MidiInputListener mil) {
        for (int i = 0; i < milCount; i++) {
            if (mils[i] == mil) {
                mils[i] = mils[i + 1];
                mil = mils[i];
            }
        }
        milCount--;
    }
