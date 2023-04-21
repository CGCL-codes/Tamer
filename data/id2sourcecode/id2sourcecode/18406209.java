    MainFrame() {
        super("GeoAPI conformance tests");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationByPlatform(true);
        desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        add(new SwingPanelBuilder().createManifestPane(title = new JLabel(), version = new JLabel(), vendor = new JLabel(), vendorID = new JLabel(), url = new JLabel(), specification = new JLabel(), specVersion = new JLabel(), specVendor = new JLabel()), BorderLayout.NORTH);
        runner = new Runner();
        results = new ResultTableModel(runner);
        final JTable table = new JTable(results);
        table.setDefaultRenderer(String.class, new ResultCellRenderer());
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getSelectionModel().addListSelectionListener(this);
        final TableColumnModel columns = table.getColumnModel();
        columns.getColumn(ResultTableModel.CLASS_COLUMN).setPreferredWidth(125);
        columns.getColumn(ResultTableModel.METHOD_COLUMN).setPreferredWidth(175);
        columns.getColumn(ResultTableModel.RESULT_COLUMN).setPreferredWidth(40);
        columns.getColumn(ResultTableModel.MESSAGE_COLUMN).setPreferredWidth(250);
        final JButton viewJavadoc = new JButton(new ImageIcon(MainFrame.class.getResource("documentinfo.png")));
        viewJavadoc.setEnabled(desktop != null && desktop.isSupported(Desktop.Action.BROWSE));
        viewJavadoc.setToolTipText("View javadoc for this test");
        viewJavadoc.addActionListener(this);
        final JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Tests", new JScrollPane(table));
        tabs.addTab("Details", new SwingPanelBuilder().createDetailsPane(testName = new JLabel(), viewJavadoc, new JTable(factories = new FactoryTableModel()), new JTable(configuration = new ConfigurationTableModel()), exception = new JTextArea()));
        add(tabs, BorderLayout.CENTER);
    }
