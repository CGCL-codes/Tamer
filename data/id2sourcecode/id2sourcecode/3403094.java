    @Override
    protected void channelAdded(String channelName) {
        model.setName(channelName);
        Channel channel = RBNBController.getInstance().getChannel(channelName);
        String unit = (channel != null) ? channel.getMetadata("units") : null;
        model.setUnit(unit);
    }
