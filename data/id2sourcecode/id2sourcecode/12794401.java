    public KMeterIndicatorPanel(MeterControls.MeterIndicator indicator) {
        super(indicator, times[timeIndex]);
        timeIndex += 1;
        timeIndex %= times.length;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        controls = (MeterControls) indicator.getParent();
        ChannelFormat channelFormat = controls.getChannelFormat();
        nchannels = channelFormat.getCount();
        movement = new MeterMovement[nchannels];
        if (channelFormat == ChannelFormat.MONO) {
            add(movement[0] = new MeterMovement(indicator));
            add(scale = new MeterScale());
        } else if (channelFormat == ChannelFormat.STEREO) {
            add(movement[0] = new MeterMovement(indicator));
            add(scale = new MeterScale());
            add(movement[1] = new MeterMovement(indicator));
        } else if (channelFormat == ChannelFormat.QUAD) {
            add(movement[2] = new MeterMovement(indicator));
            add(movement[0] = new MeterMovement(indicator));
            add(scale = new MeterScale());
            add(movement[1] = new MeterMovement(indicator));
            add(movement[3] = new MeterMovement(indicator));
        } else if (channelFormat == ChannelFormat.FIVE_1) {
            add(movement[2] = new MeterMovement(indicator));
            add(movement[0] = new MeterMovement(indicator));
            add(movement[4] = new MeterMovement(indicator));
            add(movement[1] = new MeterMovement(indicator));
            add(movement[3] = new MeterMovement(indicator));
            add(scale = new MeterScale());
            add(movement[5] = new MeterMovement(indicator));
        }
    }
