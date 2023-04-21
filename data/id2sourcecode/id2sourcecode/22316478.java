    private boolean performWRlist(CommandSender sender, String[] args, Player player) {
        ArrayList<String> list = new ArrayList<String>();
        player.sendMessage("WirelessRedstone Channel List(" + plugin.WireBox.getChannels().size() + ")");
        for (WirelessChannel channel : plugin.WireBox.getChannels()) {
            String item = channel.getName() + ": ";
            for (String owner : channel.getOwners()) {
                item += owner + ", ";
            }
            list.add(item);
            list.add("Receivers: " + channel.getReceivers().size() + " | Transmitters: " + channel.getTransmitters().size());
        }
        if (args.length >= 1) {
            int pagenumber;
            try {
                pagenumber = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage("This page number is not an number!");
                return true;
            }
            player.sendMessage("WirelessRedstone Channel List(" + plugin.WireBox.getChannels().size() + " Channels)");
            ShowList(list, pagenumber, player);
            player.sendMessage("/wrlist pagenumber for next page!");
            return true;
        } else {
            player.sendMessage("WirelessRedstone Channel List(" + plugin.WireBox.getChannels().size() + " Channels)");
            ShowList(list, 1, player);
            player.sendMessage("/wrlist pagenumber for next page!");
        }
        return false;
    }
