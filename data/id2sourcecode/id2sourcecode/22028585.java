    private JComponent buildChannelEdittingPanel() {
        StringBuffer rowDef = new StringBuffer();
        JTextField temp_tf;
        if (!(this.hardware instanceof HomenetHardware)) {
            throw new ClassCastException("Cannot cast to a type HomenetHardware");
        }
        HomenetHardware hw = (HomenetHardware) this.hardware;
        for (int channel = 0; channel < hw.getNumChannels(); channel++) {
            if (channel == 0) rowDef.append("4dlu, pref"); else rowDef.append(", 4dlu, pref");
            temp_tf = new JTextField(hw.getChannelDescription(channel));
            channelTextFields.add(temp_tf);
        }
        rowDef.append(", 4dlu");
        FormLayout panelLayout = new FormLayout("4dlu, pref, 4dlu, fill:default:grow, 4dlu", rowDef.toString());
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        for (int channel = 0; channel < hw.getNumChannels(); channel++) {
            builder.addLabel("Channel " + channel + ":", cc.xy(2, channel * 2 + 2));
            builder.add(channelTextFields.get(channel), cc.xy(4, channel * 2 + 2));
        }
        JPanel panel = builder.getPanel();
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Channel descriptions"));
        return panel;
    }
