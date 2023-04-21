    public MeterOversPanel(Control indicator) {
        super(indicator);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        controls = (MeterControls) indicator.getParent();
        ChannelFormat format = controls.getChannelFormat();
        center = format.getCenter();
        left = format.getLeft();
        right = format.getRight();
        if (left != null) {
            leftOvers = new OverLabel();
            add(leftOvers);
        }
        if (center >= 0) {
            centerOvers = new OverLabel();
            centerOvers.setHorizontalAlignment(JLabel.CENTER);
            add(centerOvers);
        }
        if (right != null) {
            rightOvers = new OverLabel();
            rightOvers.setHorizontalAlignment(JLabel.RIGHT);
            add(rightOvers);
        }
        Timer t = new Timer(500, new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                pollAndUpdate();
            }
        });
        setTimer(t);
    }
