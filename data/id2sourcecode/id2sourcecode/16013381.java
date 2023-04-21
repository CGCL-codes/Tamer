    public MixControls(MixerControls mixerControls, int stripId, BusControls busControls, boolean isMaster) {
        super(busControls.getId(), busControls.getName());
        this.mixerControls = mixerControls;
        this.busControls = busControls;
        this.isMaster = isMaster;
        int busId = busControls.getId();
        ChannelFormat format = getChannelFormat();
        channelCount = format.getCount();
        if (format.getLFE() >= 0) {
        }
        if (channelCount >= 4) {
            frontRearControl = new FrontRearControl();
            add(frontRearControl);
            derive(frontRearControl);
        }
        if (format.getCenter() >= 0 && channelCount > 1) {
        }
        if (channelCount > 1) {
            if (stripId == CHANNEL_STRIP) {
                PanControl pc = new PanControl();
                add(pc);
                lcrControl = pc;
            } else {
                BalanceControl bc = new BalanceControl();
                add(bc);
                lcrControl = bc;
            }
            derive(lcrControl);
        }
        ControlRow enables = new ControlRow();
        if (isMaster) {
            enables.add(busControls.getSoloIndicator());
        } else {
            enables.add(soloControl = createSoloControl());
            derive(soloControl);
            soloControl.addObserver(busControls);
        }
        enables.add(muteControl = createMuteControl());
        derive(muteControl);
        add(enables);
        if (busId == MAIN_BUS) {
            EnumControl routeControl = createRouteControl(stripId);
            if (routeControl != null) {
                add(routeControl);
            }
        }
        float initialdB = ((busId == AUX_BUS || busId == FX_BUS) && !isMaster) ? -FaderLaw.ATTENUATION_CUTOFF : 0f;
        gainControl = new GainControl(initialdB);
        gainControl.setInsertColor(isMaster ? Color.BLUE.darker() : Color.black);
        add(gainControl);
        derive(gainControl);
    }
