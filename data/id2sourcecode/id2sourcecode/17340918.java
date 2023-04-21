    private JComponent buildChannelEdittingPanel() {
        StringBuffer rowDef = new StringBuffer();
        JTextField temp_tf;
        for (int channel = 0; channel < hardware.getNumChannels(); channel++) {
            if (channel == 0) rowDef.append("4dlu, pref"); else rowDef.append(", 4dlu, pref");
            temp_tf = new JTextField(hardware.getChannelDescription(channel));
            channelTextFields.add(temp_tf);
        }
        rowDef.append(", 4dlu");
        FormLayout panelLayout = new FormLayout("4dlu, pref, 4dlu, 95dlu, 4dlu", rowDef.toString());
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        for (int channel = 0; channel < hardware.getNumChannels(); channel++) {
            builder.addLabel("Channel " + channel + ":", cc.xy(2, channel * 2 + 2));
            builder.add(channelTextFields.get(channel), cc.xy(4, channel * 2 + 2));
        }
        JPanel panel = builder.getPanel();
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Channel descriptions"));
        return panel;
    }
