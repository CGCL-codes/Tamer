    public List<EPGEntry> getSmartEpg(VdrSmartSearch vss) {
        ArrayList<EPGEntry> lEpgRepeats = new ArrayList<EPGEntry>();
        for (Integer chnu : htEpg.keySet()) {
            if (chnu == vss.getChannel() || !vss.isLimitToChannel()) {
                List<EPGEntry> lEpgCandidates = htEpg.get(chnu);
                for (EPGEntry epgCandidate : lEpgCandidates) {
                    boolean titel = epgCandidate.getTitle().startsWith(vss.getSuche());
                    boolean range = true;
                    if (titel && vss.isLimitRange()) {
                        range = isEpgInRange(epgCandidate, vss.getEpgStart(), vss.getRangeDown(), vss.getRangeUp());
                    }
                    if (titel && range) {
                        lEpgRepeats.add(epgCandidate);
                    }
                }
            }
        }
        return lEpgRepeats;
    }
