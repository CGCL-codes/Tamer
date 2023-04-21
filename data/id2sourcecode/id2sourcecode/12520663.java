    public static void joinChat(String zoneTag, EpicZonePlayer ezp) {
        if (Config.enableHeroChat) {
            if (EpicZones.heroChat != null && EpicZones.heroChat.isEnabled()) {
                EpicZone theZone = General.myZones.get(zoneTag);
                if (theZone != null) {
                    if (EpicZones.heroChat.getChannelManager() != null) {
                        while (EpicZones.heroChat.getChannelManager().getChannel(theZone.getTag()) == null && theZone.hasParent()) {
                            theZone = General.myZones.get(theZone.getParent().getTag());
                        }
                        if (!ezp.getPreviousZoneTag().equals(theZone.getTag())) {
                            if (EpicZones.heroChat.getChannelManager().getChannel(theZone.getTag()) != null) {
                                EpicZones.heroChat.getChannelManager().getChannel(theZone.getTag()).addPlayer(ezp.getName());
                                if (ezp.getHasMoved()) {
                                    EpicZones.heroChat.getChannelManager().setActiveChannel(ezp.getName(), zoneTag);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
