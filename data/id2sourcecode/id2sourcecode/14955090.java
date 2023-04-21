    public void setPotentialParts() {
        ArrayList<String> potentialInstruments = new ArrayList<String>();
        for (int i = 0; i < MIDIBeast.allParts.size(); i++) {
            if (MIDIBeast.allParts.get(i).getChannel() == DRUM_CHANNEL) {
                potentialInstruments.add("DRUMS");
            } else {
                potentialInstruments.add(MIDIBeast.getInstrumentForPart(i));
            }
        }
        potentialInstrumentsJList.setListData(potentialInstruments.toArray());
        potentialInstrumentsJList.setSelectedIndex(0);
    }
