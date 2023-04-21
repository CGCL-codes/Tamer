    public void setPotentialParts() {
        ArrayList<String> potentialInstruments = new ArrayList<String>();
        for (int i = 0; i < MIDIBeast.allParts.size(); i++) {
            if (MIDIBeast.allParts.get(i).getChannel() == 9) potentialInstruments.add("DRUMS"); else potentialInstruments.add(MIDIBeast.getInstrumentName(MIDIBeast.allParts.get(i).getInstrument()));
        }
        potentialInstrumentsJList.setListData(potentialInstruments.toArray());
        potentialInstrumentsJList.setSelectedIndex(0);
    }
