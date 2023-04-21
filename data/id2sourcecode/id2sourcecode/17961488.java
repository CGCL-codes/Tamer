    public static void setUpBeforeClass(String mode, String aid) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        String ch = mode;
        ChannelRegistry.getInstance().add(new PcscCardChannelFactory(), null);
        ChannelRegistry.getInstance().add(new VopCardChannelFactory(), "Simulation;localhost;2000");
        ChannelConfig[] channelConfigList = ChannelRegistry.getInstance().getChannelList();
        ChannelConfig channelConfig = null;
        for (int i = 0; i < channelConfigList.length; i++) {
            if (ch.equals(channelConfigList[i].getName())) {
                channelConfig = channelConfigList[i];
                break;
            }
        }
        if (channelConfig == null) {
            throw new RuntimeException("Selected channel not found, aborting");
        }
        channelConfig.setOpenTimeout(1000);
        channel = ChannelRegistry.getInstance().openChannel(channelConfig);
        applet = new GpApplet(channel, new Aid(aid));
        applet.select();
    }
