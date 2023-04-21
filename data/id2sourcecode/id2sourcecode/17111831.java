    public BSBSubChannelDropdownView(BSBSubChannelDropdown dropdown2) {
        this.dropdown = dropdown2;
        this.setBSBObject(dropdown2);
        this.setLayout(null);
        model = new SubChannelComboBoxModel();
        comboBox = new JComboBox(model);
        comboBox.setSelectedItem(dropdown.getChannelOutput());
        this.add(comboBox);
        comboBox.setSize(comboBox.getPreferredSize());
        this.setSize(comboBox.getPreferredSize());
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    dropdown.setChannelOutput((String) comboBox.getSelectedItem());
                }
            }
        });
        revalidate();
        dropdown.addPropertyChangeListener(this);
    }
