    public SlaveUpdater(VirtualSpace vs, boolean jmx) throws Exception {
        this.virtualSpace = vs;
        virtualSpace.setSlave(true);
        channel.connect(vs.getName());
        if (jmx) {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            JmxConfigurator.registerChannel(channel, server, "JGroupsChannel=" + channel.getChannelName(), "wild", true);
        }
        channel.setReceiver(new ReceiverAdapter() {

            @Override
            public void viewAccepted(View newView) {
                System.out.println("** view: " + newView);
            }

            @Override
            public void receive(Message msg) {
                if (!(msg.getObject() instanceof Delta)) {
                    System.out.println("wrong message type (Delta expected)");
                    return;
                }
                Delta delta = (Delta) msg.getObject();
                delta.apply(SlaveUpdater.this);
                VirtualSpaceManager.INSTANCE.repaintNow();
            }
        });
    }
