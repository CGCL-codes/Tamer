    private void initComponents() {
        fileExportChooser = new JFileChooser();
        fileExportChooser.setAcceptAllFileFilterUsed(false);
        fileField = new JTextField();
        fileField.setEditable(false);
        fileButton = new JButton("...", TimeLineGUI.readImageIcon("folder_explore.png"));
        ActionListener alFileChoose = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                chooseFile();
            }
        };
        fileButton.addActionListener(alFileChoose);
        defaultButton = new JButton("Restore defaults", TimeLineGUI.readImageIcon("action_refresh.png"));
        exportButton = new JButton("Export", TimeLineGUI.readImageIcon("accept.png"));
        cancelButton = new JButton("Cancel", TimeLineGUI.readImageIcon("cancel.png"));
        ActionListener alDefault = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doRestoreDefaults();
            }
        };
        ActionListener alExport = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (file.exists()) {
                    int yes = JOptionPane.showConfirmDialog(null, "The file \"" + file.getName() + "\" already exists. Overwrite it?", "Configm Selected File", JOptionPane.YES_NO_OPTION);
                    if (yes == JOptionPane.YES_OPTION) doExport();
                } else {
                    doExport();
                }
            }
        };
        ActionListener alCancel = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                doCancel();
            }
        };
        defaultButton.addActionListener(alDefault);
        exportButton.addActionListener(alExport);
        cancelButton.addActionListener(alCancel);
    }
