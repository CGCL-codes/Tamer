    public JPanel getPanel() {
        if (this.panel != null) {
            return this.panel;
        }
        JPanel panel = new JPanel();
        AnchorLayout panelLayout = new AnchorLayout();
        panel.setLayout(panelLayout);
        panel.setPreferredSize(new java.awt.Dimension(319, 148));
        panel.add(getAddMidiOutButton(), new AnchorConstraint(706, 963, 875, 521, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getUpdatePrefsButton(), new AnchorConstraint(706, 487, 875, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getDelayTF(), new AnchorConstraint(347, 371, 489, 268, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getDelayLabel(), new AnchorConstraint(347, 268, 489, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        this.getUpdatePrefsButton().addActionListener(this);
        this.getAddMidiOutButton().addActionListener(this);
        pageNameLBL = new JLabel("Page " + (this.index + 1) + ":  MIDI Faders");
        panel.add(pageNameLBL, new AnchorConstraint(0, 800, 82, 0, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getChannelL(), new AnchorConstraint(347, 710, 489, 500, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getChannelTF(), new AnchorConstraint(354, 813, 483, 710, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getCcOffsetLabel(), new AnchorConstraint(489, 268, 638, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getCcOffsetTF(), new AnchorConstraint(503, 371, 625, 268, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        JLabel midiout = new JLabel("MIDI Out: " + this.midiDeviceName);
        panel.add(midiout, new AnchorConstraint(179, 894, 307, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        midiout.setPreferredSize(new java.awt.Dimension(279, 19));
        pageNameLBL.setPreferredSize(new java.awt.Dimension(272, 20));
        this.panel = panel;
        return panel;
    }
