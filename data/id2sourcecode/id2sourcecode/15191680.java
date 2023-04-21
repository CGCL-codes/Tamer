    protected void initMorphParts(int i) {
        if (startScore.size() > i) from = startScore.getPart(i);
        if (endScore.size() > i) to = endScore.getPart(i);
        if (endScore.size() <= i) {
            to = emptyPart;
            to.setChannel(from.getChannel());
            to.setInstrument(from.getInstrument());
            to.setIdChannel(from.getIdChannel());
        } else if (startScore.size() <= i) {
            from = emptyPart;
            from.setChannel(to.getChannel());
            from.setInstrument(to.getInstrument());
            from.setIdChannel(to.getIdChannel());
        } else {
        }
    }
