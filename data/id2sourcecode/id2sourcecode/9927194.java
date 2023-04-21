    public PropertyDialog(JFrame parent, Property property, boolean is_dev) {
        super(parent, true);
        initComponents();
        setProperty(property);
        manageInheritanceStatus(property);
        titleLbl.setText(((is_dev) ? "Device" : "Class") + "  Property");
        if (is_dev) {
            mandatoryBtn.setToolTipText(Utils.buildToolTip("Mandatoty Device Property", "The property value must be specified in Tango database.\n" + "Otherwise all commands and read/write attribute will throw an exception."));
        } else mandatoryBtn.setVisible(false);
        pack();
        ATKGraphicsUtils.centerDialog(this);
    }
