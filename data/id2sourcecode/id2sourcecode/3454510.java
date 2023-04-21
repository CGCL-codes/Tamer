    private JPanel getChannelGraphPane() {
        if (channelGraphPane == null) {
            channelGraphPane = new JPanel();
            channelGraphPane.setLayout(new GridBagLayout());
            channelGraphPane.setBackground(Color.LIGHT_GRAY);
            JLabel prefName = new JLabel("Channel");
            channelCanvas = new ChannelCanvas(netem);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 1;
            c.gridwidth = 2;
            c.weightx = 0;
            c.weighty = 0;
            c.fill = GridBagConstraints.BOTH;
            channelGraphPane.add(prefName, c);
            c.gridx = 0;
            c.gridy = 1;
            c.gridheight = 4;
            c.gridwidth = 2;
            c.weightx = 1;
            c.weighty = 1;
            channelGraphPane.add(channelCanvas, c);
        }
        return channelGraphPane;
    }
