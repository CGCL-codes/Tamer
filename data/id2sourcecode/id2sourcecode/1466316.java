    private void loadTrackNames() {
        trackIDList.clear();
        fromCombo.removeAllItems();
        toCombo.removeAllItems();
        HashMap<String, Track> tmpTrkMap = getCopyAgent().getTrackMap();
        int cntr = 0, fromSelIndx = -1, toSelIndx = -1;
        for (String trkID : tmpTrkMap.keySet()) {
            trackIDList.add(trkID);
            fromCombo.addItem(tmpTrkMap.get(trkID).getName() + " (" + String.valueOf(tmpTrkMap.get(trkID).getChannel() + 1) + ")");
            toCombo.addItem(tmpTrkMap.get(trkID).getName() + " (" + String.valueOf(tmpTrkMap.get(trkID).getChannel() + 1) + ")");
            if (trkID.equals(getCopyAgent().getFromTrackID())) fromSelIndx = cntr;
            if (trkID.equals(getCopyAgent().getToTrackID())) toSelIndx = cntr;
            ++cntr;
        }
        if (fromSelIndx >= 0) fromCombo.setSelectedIndex(fromSelIndx);
        if (toSelIndx >= 0) toCombo.setSelectedIndex(toSelIndx);
    }
