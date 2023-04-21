    private void initialize() {
        horizontalLbl = new JLabel();
        horizontalLbl.setBounds(new Rectangle(40, 105, 136, 21));
        horizontalLbl.setText("Horizontal Mode");
        this.setSize(205, 176);
        this.setLayout(null);
        this.add(getPageLabel(), null);
        setName("MIDI Faders Page");
        this.add(getDelayTF(), null);
        this.add(getDelayLBL(), null);
        this.add(getCcOffsetLBL(), null);
        this.add(getCcOffsetTF(), null);
        this.add(getChannelLBL(), null);
        this.add(getChannelTF(), null);
        this.add(getUpdatePrefsBtn(), null);
        this.add(getHorizontalCB(), null);
        this.add(horizontalLbl, null);
    }
