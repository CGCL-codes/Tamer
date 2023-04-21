    public CounterTimer getMonitor() {
        if (monitor == null) {
            try {
                String channel = device.read_attribute(DevicePoolUtils.MEASUREMENT_GROUP_ATTR_MONITOR).extractString();
                if (channel.equalsIgnoreCase(MonitorNotInitialized.getName())) {
                    monitor = MonitorNotInitialized;
                } else {
                    monitor = null;
                    for (ExperimentChannel ch : channels) {
                        if (ch.getName().equalsIgnoreCase(channel)) {
                            assert (ch instanceof CounterTimer);
                            monitor = (CounterTimer) ch;
                        }
                    }
                    if (monitor == null) {
                        log.warning("monitor channel " + channel + " not found for " + getName() + " from list of channels:" + getChannels());
                    }
                }
            } catch (DevFailed e) {
            }
        }
        return monitor;
    }
