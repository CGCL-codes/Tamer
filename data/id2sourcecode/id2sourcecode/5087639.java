    private void init() {
        MapReader readers[];
        MapWriter writers[];
        try {
            readers = pluginLoader.getReaders();
            writers = pluginLoader.getWriters();
            String[] plugins = new String[readers.length + writers.length];
            for (int i = 0; i < readers.length; i++) {
                plugins[i] = readers[i].getPluginPackage();
            }
            for (int i = 0; i < writers.length; i++) {
                plugins[i + readers.length] = writers[i].getPluginPackage();
            }
            pluginList = new JList(plugins);
        } catch (Throwable e) {
            e.printStackTrace();
            pluginList = new JList();
        }
        pluginList.addListSelectionListener(this);
        JScrollPane pluginScrollPane = new JScrollPane(pluginList);
        pluginScrollPane.setAutoscrolls(true);
        pluginScrollPane.setPreferredSize(new Dimension(200, 150));
        infoButton = new JButton("Info");
        removeButton = new JButton("Remove");
        closeButton = new JButton("Close");
        infoButton.addActionListener(this);
        removeButton.addActionListener(this);
        closeButton.addActionListener(this);
        JPanel buttonPanel = new VerticalStaticJPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(infoButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(closeButton);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(pluginScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(buttonPanel);
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(closeButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateButtons();
    }
