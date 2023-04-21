    private void initComponents() {
        this.initMenu();
        statusField = new JLabel();
        statusField.setText(statusMessages[Client.DISCONNECTED]);
        statusColor = new JTextField(1);
        statusColor.setBackground(Color.red);
        statusColor.setEditable(false);
        statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);
        statusBar.add(statusField, BorderLayout.CENTER);
        chatPane = new JPanel(new BorderLayout());
        chatLine = new JTextField();
        chatLine.setEnabled(false);
        chatTabs = new CloseableTabbedPane();
        tabMap = new HashMap<String, ChatPane>();
        chatTabs.addCloseableTabbedPaneListener(new CloseableTabbedPaneListener() {

            public boolean closeTab(int tabIndexToClose) {
                String name = chatTabs.getComponentAt(tabIndexToClose).getName();
                if (name.charAt(0) == '#') client.getIO().write("PART " + name, IOFactory.CHATCMD); else if (name.charAt(0) == '@') client.getIO().write("GPART " + name, IOFactory.CHATCMD);
                return true;
            }
        });
        chatTabs.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                ClientView.getInstance().focusChat();
            }
        });
        console = new ChatPane("Console");
        chatTabs.addTab(console, false);
        chatLine.addActionListener(new CommandListener());
        chatPane.add(chatLine, BorderLayout.SOUTH);
        chatPane.add(chatTabs, BorderLayout.CENTER);
        chatPane.setPreferredSize(new Dimension(600, 200));
        loginPane = new JPanel();
        GridBagLayout loginPaneLayout = new GridBagLayout();
        loginPaneLayout.rowWeights = new double[] { 0.4, 0.1, 0.0, 0.0, 0.0, 0.5 };
        loginPaneLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7 };
        loginPaneLayout.columnWeights = new double[] { 1.0 };
        loginPaneLayout.columnWidths = new int[] { 7 };
        loginPane.setLayout(loginPaneLayout);
        loginPane.setPreferredSize(new Dimension(640, 480));
        loginPane.add(new JLabel("TRACTORLOL"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        ActionListener loginListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Client.getInstance().isConnected()) {
                    Client.getInstance().login(true);
                } else {
                    Client.getInstance().connect(true);
                }
                updateStatusTS();
            }
        };
        JPanel userPane = new JPanel();
        userPane.add(new JLabel("Username: "));
        nameField = new JTextField(10);
        nameField.setEnabled(true);
        nameField.addActionListener(loginListener);
        userPane.add(nameField);
        loginPane.add(userPane, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        connectButton = new JButton("Connect");
        connectButton.setMnemonic(KeyEvent.VK_C);
        connectButton.setActionCommand("connect");
        connectButton.addActionListener(loginListener);
        connectButton.setEnabled(true);
        loginPane.add(connectButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        mainPane = new JPanel(new BorderLayout());
        mainPane.add(loginPane, BorderLayout.CENTER);
        mainPane.add(statusBar, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPane);
        this.setSize(this.getPreferredSize());
        this.setLocation(200, 200);
        this.pack();
    }
