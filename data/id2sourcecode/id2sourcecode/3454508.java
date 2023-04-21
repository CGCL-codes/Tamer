    private JPanel getPreferencePane() {
        if (preferencePane == null) {
            preferencePane = new JPanel();
            preferencePane.setLayout(new GridBagLayout());
            preferencePane.setBorder(BorderFactory.createTitledBorder("Preferences"));
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 4;
            c.gridwidth = 2;
            c.weightx = 1;
            c.weighty = 1;
            c.insets = new Insets(5, 5, 5, 5);
            c.fill = GridBagConstraints.BOTH;
            preferencePane.add(getChannelGraphPane(), c);
            c.gridx = 0;
            c.gridy = 4;
            c.gridheight = 2;
            c.gridwidth = 2;
            c.weightx = 0;
            c.weighty = 0.4;
            preferencePane.add(getChannelRulePane(), c);
        }
        return preferencePane;
    }
