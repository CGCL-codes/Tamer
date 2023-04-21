    public void moveInstrument(int fromPos, int toPos) {
        Instrument tempInstr;
        if (toPos == fromPos) return;
        tempInstr = instruments[fromPos];
        changeInstrument(toPos, fromPos + OFFSET);
        if (toPos < fromPos) {
            for (int i = fromPos; i > toPos; i--) {
                instruments[i] = instruments[i - 1];
                changeInstrument(i, i - 1 + OFFSET);
            }
        } else {
            for (int i = fromPos; i < toPos; i++) {
                instruments[i] = instruments[i + 1];
                changeInstrument(i, i + 1 + OFFSET);
            }
        }
        instruments[toPos] = tempInstr;
        normalizeInstrument();
    }
