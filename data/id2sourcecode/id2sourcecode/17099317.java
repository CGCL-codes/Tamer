    public void start() {
        super.start();
        LProgressViewer.getInstance().entrySubProgress(getName());
        LProgressViewer.getInstance().entrySubProgress(0.7);
        ALayer l = getSelectedLayer();
        AChannelMarker m = l.getSelectedChannel().getMarker();
        int n = m.getNumberOfMarkers();
        int lastMarker = 0;
        for (int i = 0; i < n + 1; i++) {
            if (LProgressViewer.getInstance().setProgress(1.0 * i / (n + 1))) return;
            int currentMarker;
            if (i < n) {
                currentMarker = m.getMarkerX(i);
            } else {
                currentMarker = l.getSelectedChannel().getSampleLength();
            }
            AClip nc = new AClip(1, 0, 0);
            nc.copyAllAttributes(getFocussedClip());
            for (int j = 0; j < l.getNumberOfChannels(); j++) {
                AChannelSelection chs = new AChannelSelection(l.getChannel(j), lastMarker, currentMarker - lastMarker);
                nc.getLayer(0).add(new AChannel(chs));
            }
            nc.getAudio().setLoopEndPointer(nc.getLayer(0).getMaxSampleLength());
            nc.setDefaultName();
            Laoe.getInstance().addClipFrame(nc);
            autoScaleFocussedClip();
            lastMarker = currentMarker;
        }
        LProgressViewer.getInstance().exitSubProgress();
        LProgressViewer.getInstance().exitSubProgress();
    }
