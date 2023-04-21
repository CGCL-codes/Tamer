    public MemoryProcessVariable makePV(final DefaultServerImpl server, final SignalEntry entry) {
        final String signalName = entry.getSignal();
        final int size = entry.getSignal().matches(".*(TBT|A)") ? DEFAULT_ARRAY_SIZE : 1;
        final MemoryProcessVariable mpv = new MemoryProcessVariable(signalName, null, DBR_Double.TYPE, new double[size]);
        mpv.setUnits("units");
        appendLimits(entry, mpv);
        if (size == 1) {
            final String[] warningPVs = ChannelFactory.defaultFactory().getChannel(signalName).getWarningLimitPVs();
            server.registerProcessVaribale(new MemoryProcessVariable(warningPVs[0], null, DBR_Double.TYPE, new double[1]));
            server.registerProcessVaribale(new MemoryProcessVariable(warningPVs[1], null, DBR_Double.TYPE, new double[1]));
            final String[] alarmPVs = ChannelFactory.defaultFactory().getChannel(signalName).getAlarmLimitPVs();
            server.registerProcessVaribale(new MemoryProcessVariable(alarmPVs[0], null, DBR_Double.TYPE, new double[1]));
            server.registerProcessVaribale(new MemoryProcessVariable(alarmPVs[1], null, DBR_Double.TYPE, new double[1]));
            final String[] operationLimitPVs = ChannelFactory.defaultFactory().getChannel(signalName).getOperationLimitPVs();
            server.registerProcessVaribale(new MemoryProcessVariable(operationLimitPVs[0], null, DBR_Double.TYPE, new double[] { mpv.getLowerDispLimit().doubleValue() }));
            server.registerProcessVaribale(new MemoryProcessVariable(operationLimitPVs[1], null, DBR_Double.TYPE, new double[] { mpv.getUpperDispLimit().doubleValue() }));
            final String[] driveLimitPVs = ChannelFactory.defaultFactory().getChannel(signalName).getDriveLimitPVs();
            server.registerProcessVaribale(new MemoryProcessVariable(driveLimitPVs[0], null, DBR_Double.TYPE, new double[] { mpv.getLowerCtrlLimit().doubleValue() }));
            server.registerProcessVaribale(new MemoryProcessVariable(driveLimitPVs[1], null, DBR_Double.TYPE, new double[] { mpv.getUpperCtrlLimit().doubleValue() }));
        }
        server.registerProcessVaribale(mpv);
        return mpv;
    }
