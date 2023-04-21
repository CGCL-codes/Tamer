    private void setChannel() {
        try {
            int t = App.UI.getTrackSelection().iterator().next();
            channel_no = App.Project.getTrackChannel(t);
        } catch (NoSuchElementException e) {
            channel_no = 0;
        } catch (NoChannelAssignedException e) {
            channel_no = 0;
        }
        channel = App.Project.getSynthesizer().getChannels()[channel_no];
    }
