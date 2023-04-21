    protected int execute(ActionData actionData) {
        Caret caret = getEditor().getTablature().getCaret();
        int fret = this.number;
        int string = caret.getSelectedString().getNumber();
        long start = caret.getPosition();
        long time = System.currentTimeMillis();
        if (this.number < 10) {
            if (lastAddedStart == start && lastAddedString == string) {
                if (lastAddedFret > 0 && lastAddedFret < 10 && time < (lastAddedTime + DELAY)) {
                    int newFret = ((lastAddedFret * 10) + fret);
                    if (newFret < 30 || getSongManager().isPercussionChannel(caret.getTrack().getChannelId())) {
                        fret = newFret;
                    }
                }
            }
            lastAddedFret = fret;
            lastAddedStart = start;
            lastAddedString = string;
            lastAddedTime = time;
        }
        actionData.put(PROPERTY_START, new Long(start));
        actionData.put(PROPERTY_FRET, new Integer(fret));
        actionData.put(PROPERTY_STRING, new Integer(string));
        return super.execute(actionData);
    }
