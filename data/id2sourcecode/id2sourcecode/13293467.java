    private void sched(ChannelRecord record, long delay, long period) {
        record.setCanceled(false);
        ChannelIF channel = record.getChannel();
        SchedulerTask tt = new SchedulerTask(record);
        synchronized (timers) {
            timers.put(channel, tt);
        }
        timer.schedule(tt, delay, period);
    }
