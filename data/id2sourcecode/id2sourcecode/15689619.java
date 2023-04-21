    private void init() {
        this.rememberSelection = new JCheckBox("Remember my decision for other entities.");
        this.rememberSelection.setSelected(true);
        this.overwriteButton = new JButton("Overwrite");
        this.skipButton = new JButton("Skip");
        this.stopButton = new JButton("Stop");
        this.message = new JLabel("DataMap already contains this table. Overwrite?");
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        messagePanel.add(message);
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        checkPanel.add(rememberSelection);
        JPanel buttons = PanelFactory.createButtonPanel(new JButton[] { skipButton, overwriteButton, stopButton });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(messagePanel, BorderLayout.NORTH);
        contentPane.add(checkPanel, BorderLayout.CENTER);
        contentPane.add(buttons, BorderLayout.SOUTH);
        setModal(true);
        setResizable(false);
        setSize(250, 150);
        setTitle("DbEntity Already Exists");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
