    public void loadMenu() {
        melodies = midiImport.getMelodies();
        if (melodies != null) {
            setResolution();
            trackListModel.clear();
            int channelNumber = 0;
            for (final MidiImportRecord record : melodies) {
                if (record.getChannel() > channelNumber) {
                    trackListModel.addElement("-------------------------------------");
                    channelNumber = record.getChannel();
                }
                trackListModel.addElement(record);
            }
            selectTrack(0);
        }
    }
