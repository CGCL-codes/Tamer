    public XMLTVGrabberTask getGrabberTask(int day, Channel channel) {
        if (log.isDebugEnabled()) {
            log.debug("Making task for day: " + day + " of channel " + channel.getChannelId() + "-" + channel.getChannelName());
        }
        XMLTVGrabberTask task = new XMLTVGrabberTask();
        task.setDay(day);
        task.setMaxOverlap(getMaxOverlap());
        task.setOverlapFixMode(getOverlapFixMode());
        task.setStarRatingGanres(getStarRatingGanres());
        task.setCache(getCache());
        task.setImdbAccess(getImdbAccess());
        task.setChannel(channel);
        return task;
    }
