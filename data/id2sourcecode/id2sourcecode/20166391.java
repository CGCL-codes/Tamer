    public Timer(PVRState context, RecordingDescription desc, int slot) {
        super(context);
        SystemInformation sysInfo = context.getSysInfo();
        Station station = context.getStation(desc.getServiceType(), desc.getServiceId());
        ResourceLocator locator = this.getLocator();
        Resource resource = locator.createActiveRecording(sysInfo.getResource(), slot);
        Resource interval = locator.createInterval(desc.getStart(), desc.getFinish());
        Resource event = locator.findEvent(interval, station.getChannel());
        locator.create(resource, TMSNet.station, station.getResource());
        locator.create(resource, TMSNet.recorder, sysInfo.getResource());
        locator.create(resource, NetEvent.time, interval);
        locator.create(resource, TMSNet.fileName, desc.getFileName());
        locator.create(resource, TMSNet.timerSlot, slot);
        locator.create(resource, TMSNet.timerTuner, desc.getTuner());
        locator.create(resource, TMSNet.recordingType, desc.getRecordingType());
        locator.createDuration(resource, TMSNet.timerRecorded, 0, desc.getRecorded());
        if (event != null) locator.create(resource, TMSNet.records, event);
        this.setResource(resource);
    }
