    public List<EPGEntry> getEpgRepeats(EPGEntry epg, boolean allChannels) {
        ArrayList<EPGEntry> lEpgRepeats = new ArrayList<EPGEntry>();
        for (List<EPGEntry> lEpgCandidates : htEpg.values()) {
            for (EPGEntry epgCandidate : lEpgCandidates) {
                boolean titel = epgCandidate.getTitle().equals(epg.getTitle());
                boolean channel = epgCandidate.getChannelName().equals(epg.getChannelName());
                boolean later = epgCandidate.getStartTime().getTimeInMillis() > epg.getStartTime().getTimeInMillis();
                if ((channel || allChannels) && titel && later) {
                    lEpgRepeats.add(epgCandidate);
                }
            }
        }
        return lEpgRepeats;
    }
