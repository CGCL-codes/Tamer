    public static void leaveChat(String zoneTag, Player player) {
        if (General.config.enableHeroChat) {
            if (EpicZones.heroChat != null && EpicZones.heroChat.isEnabled()) {
                if (EpicZones.heroChat.getChannelManager().getChannel(zoneTag) != null) {
                    EpicZones.heroChat.getChannelManager().getChannel(zoneTag).removePlayer(player.getName());
                }
            }
        }
    }
