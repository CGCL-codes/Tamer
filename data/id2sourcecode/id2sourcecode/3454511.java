    private JPanel getChannelRulePane() {
        if (channelRulePane == null) {
            channelRulePane = new JPanel();
            channelRulePane.setLayout(new GridBagLayout());
            channelRulePane.setBackground(Color.WHITE);
            channelRule = new JTextArea(10, 100);
            channelRule.setEditable(false);
            channelRule.setText("No context selected");
            JScrollPane scroll = new JScrollPane(channelRule);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 2;
            c.gridwidth = 2;
            c.weightx = 1;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            channelRulePane.add(scroll, c);
        }
        return channelRulePane;
    }
