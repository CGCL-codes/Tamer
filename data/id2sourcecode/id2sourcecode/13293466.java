    private void resched(ChannelRecord record, long period) {
        ChannelIF channel = record.getChannel();
        unschedule(channel);
        sched(record, 0, period);
    }
