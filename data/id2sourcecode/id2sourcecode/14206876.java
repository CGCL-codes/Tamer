    public void acceptAsCompatible(InkTraceFormat format, boolean strict) throws InkMLComplianceException {
        for (InkChannel channel : channels) {
            if (!channel.isIntermittent() && !format.containsChannel(channel.getName())) {
                throw new InkMLComplianceException(String.format("Channel '%s' is not present", channel.getName().toString()));
            }
            if (format.containsChannel(channel.getName())) {
                channel.acceptAsCompatible(format.getChannel(channel.getName()), strict);
            }
        }
    }
