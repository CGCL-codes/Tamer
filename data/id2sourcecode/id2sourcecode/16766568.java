    public JPanel getPanel() {
        if (this.panel != null) {
            return this.panel;
        }
        JPanel panel = new JPanel();
        AnchorLayout panelLayout = new AnchorLayout();
        panel.setLayout(panelLayout);
        panel.setPreferredSize(new java.awt.Dimension(490, 175));
        panel.add(getBankSizeTF(), new AnchorConstraint(717, 603, 837, 543, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getBankSizeLabel(), new AnchorConstraint(734, 519, 814, 380, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getJPanel1(), new AnchorConstraint(117, 947, 700, 15, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        pageNameLBL = new JLabel("Page " + (this.index + 1) + ": MIDI Sequencer");
        panel.add(pageNameLBL, new AnchorConstraint(0, 382, 82, 0, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getUpdatePrefsButton(), new AnchorConstraint(717, 345, 837, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getAddMidiOutButton(), new AnchorConstraint(865, 345, 985, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getChannelL(), new AnchorConstraint(865, 519, 945, 380, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        panel.add(getChannelTF(), new AnchorConstraint(848, 603, 968, 543, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        JLabel midiout = new JLabel("MIDI Out: " + this.midiDeviceName);
        panel.add(midiout, new AnchorConstraint(2, 786, 82, 419, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
        pageNameLBL.setPreferredSize(new java.awt.Dimension(180, 14));
        this.getAddMidiOutButton().addActionListener(this);
        this.getUpdatePrefsButton().addActionListener(this);
        this.panel = panel;
        return panel;
    }
