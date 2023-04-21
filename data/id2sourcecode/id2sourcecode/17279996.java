    public void updateEpgForChannel(int chNu, List<EPGEntry> lEpg) {
        if (lEpg.size() > 0) {
            htEpg.put(chNu, lEpg);
            EPGEntry epg = lEpg.get(0);
            htChannelTransponder.put(epg.getChannelID(), chNu);
            htChannelName.put(epg.getChannelName(), chNu);
            logger.debug("Update Channel EPG for channel " + chNu + " (" + epg.getChannelID() + "), " + lEpg.size() + " entries");
        } else {
            logger.warn("No EPG entries for channel " + chNu);
        }
    }
