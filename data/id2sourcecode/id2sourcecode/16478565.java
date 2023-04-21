    public static boolean getChannelMonitorStatus() {
        String monitorChannels = System.getProperty(Arguments.CFG_MONITOR_CHANNELS, "true");
        return Boolean.parseBoolean(monitorChannels);
    }
