    public Timer(PVRState context, TimerDescription desc) {
        super(context);
        SystemInformation sysInfo = context.getSysInfo();
        Station station = context.getStation(desc.getServiceType(), desc.getServiceId());
        ResourceLocator locator = this.getLocator();
        Resource resource = locator.createPendingRecording(sysInfo.getResource(), desc.getSlot());
        Resource interval = locator.createInterval(desc.getStart(), desc.getFinish());
        Resource event = locator.findEvent(interval, station.getChannel());
        locator.create(resource, TMSNet.station, station.getResource());
        locator.create(resource, TMSNet.recorder, sysInfo.getResource());
        locator.create(resource, NetEvent.time, interval);
        locator.create(resource, TMSNet.timerRecording, desc.isRecording());
        locator.create(resource, TMSNet.fixFileName, desc.isFixFileName());
        locator.create(resource, TMSNet.fileName, desc.getFileName());
        locator.create(resource, TMSNet.timerSlot, desc.getSlot());
        locator.create(resource, TMSNet.timerTuner, desc.getTuner());
        locator.create(resource, TMSNet.repeat, desc.getReservationType());
        if (event != null) locator.create(resource, TMSNet.records, event);
        this.setResource(resource);
    }
