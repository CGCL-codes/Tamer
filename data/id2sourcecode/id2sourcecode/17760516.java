    private void addInfo(StringBuffer sb, Recording recording) {
        Duration duration = recording.getDuration();
        Duration totalDuration = recording.getTotalDuration();
        String info = recording.getChannelName() + ", " + duration.getHours() + ":" + fillWithSpace(duration.getMinutes()) + " (" + totalDuration.getHours() + ":" + fillWithSpace(totalDuration.getMinutes()) + ")";
        this.newLine(sb, info);
    }
