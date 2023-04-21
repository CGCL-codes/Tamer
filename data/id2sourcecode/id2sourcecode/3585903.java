    private void init() {
        setLayout(new GridBagLayout());
        read.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!read.isSelected() && !write.isSelected()) {
                    ok.setEnabled(false);
                } else {
                    ok.setEnabled(true);
                }
            }
        });
        write.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!read.isSelected() && !write.isSelected()) {
                    ok.setEnabled(false);
                } else {
                    ok.setEnabled(true);
                }
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 3, 5);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(read, constraints);
        constraints.insets = new Insets(0, 10, 5, 5);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(write, constraints);
    }
