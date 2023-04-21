    public static void leaveChat(String zoneTag, Player player) {
        if (General.config.enableHeroChat) {
            if (EpicZones.heroChat != null) {
                if (EpicZones.heroChat.getChannel(zoneTag) != null) {
                    EpicZones.heroChat.getChannel(zoneTag).removePlayer(player);
                }
            }
        }
    }
