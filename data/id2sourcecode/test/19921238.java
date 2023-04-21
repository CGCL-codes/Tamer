    public JPanel getPanel() {
        if (panel != null) {
            if (lastMsgLbl != null) {
                gridPanel.remove(lastMsgLbl);
            }
            lastMsgLbl = new JLabel("Last MIDI Message: Channel " + this.lastMIDIChannel + ", Note: " + this.lastMIDINote);
            gridPanel.add(lastMsgLbl);
            return panel;
        }
        this.midiChannels = new ArrayList<JTextField>();
        this.midiNotes = new ArrayList<JTextField>();
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 1));
        JCheckBox checkBox = new JCheckBox();
        checkBox.setText("Enable Page Change Button");
        checkBox.setName("PageChangeButton");
        checkBox.setSelected(this.monome.usePageChangeButton);
        checkBox.addActionListener(this);
        gridPanel.add(checkBox);
        checkBox = new JCheckBox();
        checkBox.setText("Enable MIDI Page Changing");
        checkBox.setName("MIDIPageChange");
        checkBox.setSelected(this.monome.useMIDIPageChanging);
        checkBox.addActionListener(this);
        gridPanel.add(checkBox);
        panel.add(gridPanel);
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BoxLayout(pagePanel, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < this.monome.pages.size() - 1; i++) {
            int note = 0;
            int channel = 0;
            if (this.monome.midiPageChangeRules.size() > i) {
                MIDIPageChangeRule mpcr = this.monome.midiPageChangeRules.get(i);
                note = mpcr.getNote();
                channel = mpcr.getChannel();
            }
            JPanel subPanel = new JPanel();
            subPanel.setLayout(new GridLayout(1, 1));
            Page page = this.monome.pages.get(i);
            String pageName = page.getName();
            JLabel label = new JLabel(pageName, JLabel.LEFT);
            subPanel.add(label);
            label = new JLabel("MIDI Channel", JLabel.RIGHT);
            subPanel.add(label);
            JTextField tf = new JTextField();
            tf.setName("" + i);
            tf.setText("" + channel);
            this.midiChannels.add(i, tf);
            subPanel.add(tf);
            label = new JLabel("MIDI Note", JLabel.RIGHT);
            subPanel.add(label);
            label.setAlignmentX(Component.RIGHT_ALIGNMENT);
            tf = new JTextField();
            tf.setName("" + i);
            tf.setText("" + note);
            this.midiNotes.add(i, tf);
            subPanel.add(tf);
            gridPanel.add(subPanel);
        }
        saveBtn = new JButton("Click to save and exit config mode.");
        gridPanel.add(saveBtn);
        this.saveBtn.addActionListener(this);
        panel.add(gridPanel);
        return panel;
    }
