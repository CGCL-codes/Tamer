    public KMeterPanel(MeterControls mc, int axis) {
        super(mc, axis, null, factory, true, true);
        controls = mc;
        if (controls.getChannelFormat() != ChannelFormat.STEREO) {
            System.out.println("WARNING: MeterPanel only handling first 2 channels");
        }
    }
