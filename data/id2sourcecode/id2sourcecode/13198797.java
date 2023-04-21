    public void init(ChannelService service) {
        this.service = service;
        for (Region region : service.getRegions()) {
            TreeItem regionItem = new TreeItem(new Label(region.getName()));
            tree.addItem(regionItem);
            for (String network : service.getNetworks(region.getCode())) {
                TreeItem networkItem = new TreeItem(new Label(network));
                regionItem.addItem(networkItem);
                for (Channel channel : service.getChannels(network)) {
                    CheckBox checkbox = new CheckBox(channel.getName());
                    channel2checkbox.put(channel.getCode(), checkbox);
                    TreeItem channelItem = new TreeItem(checkbox);
                    networkItem.addItem(channelItem);
                }
            }
        }
    }
