    public static void leaveChat(String zoneTag, EpicZonePlayer ezp) {
        if (Config.enableHeroChat) {
            if (EpicZones.heroChat != null && EpicZones.heroChat.isEnabled()) {
                if (EpicZones.heroChat.getChannelManager().getChannel(zoneTag) != null) {
                    EpicZones.heroChat.getChannelManager().getChannel(zoneTag).removePlayer(ezp.getName());
                    EpicZones.heroChat.getChannelManager().setActiveChannel(ezp.getName(), EpicZones.heroChat.getChannelManager().getDefaultChannel().getName());
                }
            }
        }
    }
