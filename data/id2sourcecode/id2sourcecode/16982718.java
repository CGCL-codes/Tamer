    public static void joinChat(String zoneTag, EpicZonePlayer ezp, Player player) {
        if (General.config.enableHeroChat) {
            if (EpicZones.heroChat != null) {
                EpicZone theZone = General.myZones.get(zoneTag);
                while (EpicZones.heroChat.getChannel(theZone.getTag()) == null && theZone.hasParent()) {
                    theZone = General.myZones.get(theZone.getParent().getTag());
                }
                if (!ezp.getPreviousZoneTag().equals(theZone.getTag())) {
                    EpicZones.heroChat.getChannel(theZone.getTag()).addPlayer(player);
                    EpicZones.heroChat.setActiveChannel(player, EpicZones.heroChat.getChannel(zoneTag));
                }
            }
        }
    }
