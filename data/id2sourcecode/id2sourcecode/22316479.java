    private boolean performChannelAdmin(CommandSender sender, String[] args, Player player) {
        if (args.length >= 3) {
            String channelname = args[0];
            String subcommand = args[1];
            String playername = args[2];
            if (subcommand.equalsIgnoreCase("addowner")) {
                if (plugin.WireBox.hasAccessToChannel(player, channelname)) {
                    WirelessChannel channel = plugin.WireBox.getChannel(channelname);
                    channel.addOwner(playername);
                    plugin.WireBox.SaveChannel(channel);
                    return true;
                } else {
                    player.sendMessage("[WirelessRedstone] You don't have access to this channel.");
                }
            } else if (subcommand.equalsIgnoreCase("removeowner")) {
                if (plugin.WireBox.hasAccessToChannel(player, channelname)) {
                    WirelessChannel channel = plugin.WireBox.getChannel(channelname);
                    channel.removeOwner(playername);
                    plugin.WireBox.SaveChannel(channel);
                    return true;
                } else {
                    player.sendMessage("[WirelessRedstone] You don't have access to this channel.");
                }
            } else {
                player.sendMessage("[WirelessRedstone] Unknown sub command!");
            }
        } else {
            player.sendMessage("Channel Admin Commands:");
            player.sendMessage("/WRc channelname addowner playername - Add a player to channel.");
            player.sendMessage("/WRc channelname removeowner playername - Add a player to channel.");
        }
        return true;
    }
