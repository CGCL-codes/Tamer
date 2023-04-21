    public Nuevo() throws Exception {
        super("Nuevo Frame");
        this.setSize(500, 300);
        this.addWindowListener(new BasicWindowMonitor());
        this.init();
        channelTree = new ChannelTree(registry.getChannels());
        JScrollPane scrollChannels = new JScrollPane(channelTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        channelTree.addTreeSelectionListener(new ChannelSelectionListener());
        itemTable = new ItemTable((ChannelIF) registry.getChannels().iterator().next());
        JScrollPane scrollItems = new JScrollPane(itemTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel lsm = itemTable.getSelectionModel();
        lsm.addListSelectionListener(new ItemSelectionListener(lsm));
        itemPane = new ItemTextPane();
        JScrollPane scrollItem = new JScrollPane(itemPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSplitPane spB = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollItems, scrollItem);
        JSplitPane spA = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollChannels, spB);
        getContentPane().add(spA, BorderLayout.CENTER);
        getContentPane().add(spB, BorderLayout.EAST);
        this.pack();
    }
