    public boolean access(String channelId) {
        if (channelId != null) {
            TimeChannel timeChannel = channels.get(channelId);
            if (timeChannel != null) {
                synchronized (timeChannel) {
                    TimerTask timerTask = timeChannel.getTimerTask();
                    if (timerTask != null) {
                        log.debug("Canceling TimerTask: %s", timerTask);
                        timerTask.cancel();
                        timeChannel.setTimerTask(null);
                    }
                    timerTask = new ChannelTimerTask(this, channelId);
                    timeChannel.setTimerTask(timerTask);
                    long timeout = gravityConfig.getChannelIdleTimeoutMillis();
                    log.debug("Scheduling TimerTask: %s for %s ms.", timerTask, timeout);
                    channelsTimer.schedule(timerTask, timeout);
                    return true;
                }
            }
        }
        return false;
    }
