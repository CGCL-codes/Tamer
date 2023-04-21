    private void pushSegmentToRbnb() {
        if (pushArgsDialog == null) {
            pushArgsDialog = new SegmentPushArgsDialog(this, archiveCover, currentSegment);
            pushArgsDialog.start();
        } else {
            pushArgsDialog.restart(pushArgsDialog.getHost(), pushArgsDialog.getPort(), currentSegment);
        }
        if (pushArgsDialog.cancled()) {
            return;
        }
        String host = pushArgsDialog.getHost();
        String port = pushArgsDialog.getPort();
        ArchiveSegmentInterface segment = pushArgsDialog.getSegment();
        String sourceName = pushArgsDialog.getSource();
        String channelName = pushArgsDialog.getChannel();
        ArchiveToRbnb doit = new ArchiveToRbnb();
        if (doit.setup(host, port, sourceName, channelName, segment) && doit.connect()) doit.startThread();
    }
