    public CounterTimer getTimer() {
        if (timer == null) {
            try {
                String channel = device.read_attribute(DevicePoolUtils.MEASUREMENT_GROUP_ATTR_TIMER).extractString();
                if (channel.equalsIgnoreCase(TimerNotInitialized.getName())) {
                    timer = TimerNotInitialized;
                } else {
                    timer = null;
                    for (ExperimentChannel ch : channels) {
                        if (ch.getName().equalsIgnoreCase(channel)) {
                            assert (ch instanceof CounterTimer);
                            timer = (CounterTimer) ch;
                        }
                    }
                    if (timer == null) {
                        log.warning("timer channel " + channel + " not found for " + getName() + " from list of channels:" + getChannels());
                    }
                }
            } catch (DevFailed e) {
                log.warning("Error trying to get Timer channel from " + getName());
            }
        }
        return timer;
    }
