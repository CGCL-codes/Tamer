    private void sendAllMessage(Player player, String message) {
        for (int i = 0; i < plugin.bot.getChannels().length; i++) {
            plugin.bot.sendMessage(plugin.bot.getChannels()[i], "<" + player.getDisplayName() + ">" + message);
        }
    }
