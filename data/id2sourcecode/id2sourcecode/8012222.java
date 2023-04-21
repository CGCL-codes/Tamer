    private void initializeValues() {
        if (monome != null) {
            ArrayList<MIDIPageChangeRule> midiPageChangeRules = monome.midiPageChangeRules;
            for (int i = 0; i < midiPageChangeRules.size(); i++) {
                midiChannels[i] = midiPageChangeRules.get(i).getChannel();
                midiNotes[i] = midiPageChangeRules.get(i).getNote();
                midiCCs[i] = midiPageChangeRules.get(i).getCC();
                midiCCVals[i] = midiPageChangeRules.get(i).getCCVal();
                pageChangeDelays[i] = monome.pageChangeDelays[i];
                linkedDevices[i] = midiPageChangeRules.get(i).getLinkedSerial();
                linkedPages[i] = midiPageChangeRules.get(i).getLinkedPageIndex();
            }
        } else if (arc != null) {
            ArrayList<MIDIPageChangeRule> midiPageChangeRules = arc.midiPageChangeRules;
            for (int i = 0; i < midiPageChangeRules.size(); i++) {
                midiChannels[i] = midiPageChangeRules.get(i).getChannel();
                midiNotes[i] = midiPageChangeRules.get(i).getNote();
                midiCCs[i] = midiPageChangeRules.get(i).getCC();
                midiCCVals[i] = midiPageChangeRules.get(i).getCCVal();
            }
        }
    }
