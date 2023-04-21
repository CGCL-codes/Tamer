    public GSegmentEditor() {
        clip = new AClip(1, 1, 100);
        segments = new GEditableSegments();
        segments.setChannel(clip.getLayer(0).getChannel(0));
        addMouseListener(this);
        addMouseMotionListener(this);
    }
