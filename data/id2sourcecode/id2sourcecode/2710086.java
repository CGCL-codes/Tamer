    private boolean repairAxisMirroring() throws InkMLComplianceException {
        boolean res = false;
        InkContext c = doc.getCurrentViewRoot().getContext();
        InkCanvasTransform tf = c.getCanvasTransform();
        List<InkChannel.ChannelName> invertChannels = new ArrayList<InkChannel.ChannelName>();
        for (InkChannel channel : c.getCanvasTraceFormat().getChannels()) {
            if (channel.getOrientation() == InkChannel.Orientation.M) {
                invertChannels.add(channel.getName());
                channel.setOrientation(InkChannel.Orientation.P);
            }
        }
        for (InkChannel.ChannelName name : invertChannels) {
            tf.invertAxis(c.getSourceFormat(), c.getCanvasTraceFormat(), name);
            System.out.println("repair canvas traceFormat: orientation -ve of channel " + name + " tranfered to canvasTranform.");
            res = true;
        }
        doc.getInk().reloadTraces();
        return res;
    }
