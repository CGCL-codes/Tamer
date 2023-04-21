    public DFSExplorer() {
        super("DFS Explorer");
        BorderLayout layout = new BorderLayout();
        sep = DFSFile.separator;
        root = new DFSFile("JFS", ROOT);
        current = new DFSFile("JFS", ROOT);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenuItem newItem = new JMenuItem("New Directory");
        newItem.setMnemonic('N');
        newItem.addActionListener(new NewDirectoryActionListener());
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        JMenuItem uploadItem = new JMenuItem("Upload");
        uploadItem.setMnemonic('U');
        uploadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        uploadItem.addActionListener(new UploadActionListener(explorer, MAX_RW_LENGTH));
        fileMenu.add(uploadItem);
        downloadItem = new JMenuItem("Download");
        downloadItem.setMnemonic('w');
        downloadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        downloadItem.addActionListener(new DownloadActionListener(MAX_RW_LENGTH, explorer));
        fileMenu.add(downloadItem);
        fileMenu.addSeparator();
        openItem = new JMenuItem("Open");
        openItem.setMnemonic('O');
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        openItem.addActionListener(new OpenActionListener());
        fileMenu.add(openItem);
        deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic('D');
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteItem.addActionListener(new DeleteActionListener());
        fileMenu.add(deleteItem);
        renameItem = new JMenuItem("Rename");
        renameItem.setMnemonic('m');
        renameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        renameItem.addActionListener(new RenameActionListener());
        fileMenu.add(renameItem);
        propertiesItem = new JMenuItem("Properties");
        propertiesItem.setMnemonic('r');
        propertiesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.ALT_MASK));
        propertiesItem.addActionListener(new PropertiesActionListener());
        fileMenu.add(propertiesItem);
        fileMenu.addSeparator();
        addConnectionItem = new JMenuItem("Add connection");
        addConnectionItem.setMnemonic('A');
        addConnectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        addConnectionItem.addActionListener(new AddConnectionActionListener());
        fileMenu.add(addConnectionItem);
        fileMenu.addSeparator();
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.setMnemonic('C');
        closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        closeItem.addActionListener(new CloseActionListener());
        fileMenu.add(closeItem);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setMnemonic('C');
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyItem.addActionListener(new CopyActionListener());
        editMenu.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setMnemonic('P');
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteItem.addActionListener(new PasteActionListener(MAX_RW_LENGTH, this));
        editMenu.add(pasteItem);
        menuBar.add(editMenu);
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        JMenu gotoMenu = new JMenu("Go To");
        gotoMenu.setMnemonic('o');
        viewMenu.add(gotoMenu);
        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.setMnemonic('H');
        homeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, ActionEvent.ALT_MASK));
        homeItem.addActionListener(new HomeActionListener());
        gotoMenu.add(homeItem);
        JMenuItem upItem = new JMenuItem("Up One Level");
        upItem.setMnemonic('U');
        upItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.ALT_MASK));
        upItem.addActionListener(new UpActionListener());
        gotoMenu.add(upItem);
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.setMnemonic('R');
        refreshItem.addActionListener(new RefreshActionListener());
        viewMenu.add(refreshItem);
        menuBar.add(viewMenu);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem aboutItem = new JMenuItem("About DFS Explorer");
        aboutItem.setMnemonic('A');
        aboutItem.addActionListener(new AboutActionListener(VERSION));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        fileList = new JList();
        fileList.setVisibleRowCount(-1);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
        fileList.setCellRenderer(new FileListCellRenderer());
        fileList.addListSelectionListener(new ExplorerListListener());
        MouseListener fileClickListener = new MouseAdapter() {

            public void mouseClicked(final MouseEvent e) {
                int index = fileList.locationToIndex(e.getPoint());
                fileList.setSelectedIndex(index);
                setControls(true);
                try {
                    selected = (DFSFileInfo) fileList.getSelectedValue();
                    if (e.getClickCount() == 2) {
                        if (selected.isDirectory()) {
                            open(new DFSFile("JFS", selected.getPath()));
                        } else {
                            FileDownloadTask task = new FileDownloadTask(explorer, current, selected, MAX_RW_LENGTH);
                            task.start();
                        }
                    }
                } catch (DFSException ex) {
                    logger.debug(ex);
                }
            }
        };
        fileList.addMouseListener(fileClickListener);
        JPopupMenu popupMenu = new JPopupMenu();
        downloadItemPU = new JMenuItem("Download");
        downloadItemPU.addActionListener(new DownloadActionListener(MAX_RW_LENGTH, explorer));
        popupMenu.add(downloadItemPU);
        popupMenu.addSeparator();
        renameItemPU = new JMenuItem("Rename");
        renameItemPU.addActionListener(new RenameActionListener());
        popupMenu.add(renameItemPU);
        deleteItemPU = new JMenuItem("Delete");
        deleteItemPU.addActionListener(new DeleteActionListener());
        popupMenu.add(deleteItemPU);
        propertiesItemPU = new JMenuItem("Properties");
        propertiesItemPU.addActionListener(new PropertiesActionListener());
        popupMenu.add(propertiesItemPU);
        MouseListener popupListener = new PopupListener(popupMenu, fileList);
        fileList.addMouseListener(popupListener);
        ImageIcon upIcon = createImageIcon("up.gif", "Up");
        JButton upButton = new JButton(upIcon);
        upButton.setToolTipText("Up");
        upButton.addActionListener(new UpActionListener());
        ImageIcon refreshIcon = createImageIcon("refresh.gif", "Refresh");
        JButton refreshButton = new JButton(refreshIcon);
        refreshButton.setToolTipText("Refresh");
        refreshButton.addActionListener(new RefreshActionListener());
        ImageIcon homeIcon = createImageIcon("home.gif", "Home");
        JButton homeButton = new JButton(homeIcon);
        homeButton.setToolTipText("Home");
        homeButton.addActionListener(new HomeActionListener());
        ImageIcon propertiesIcon = createImageIcon("properties.gif", "Properties");
        propertiesButton = new JButton(propertiesIcon);
        propertiesButton.setToolTipText("Properties");
        propertiesButton.addActionListener(new PropertiesActionListener());
        ImageIcon copyIcon = createImageIcon("copy.gif", "Copy");
        copyButton = new JButton(copyIcon);
        copyButton.setToolTipText("Copy");
        copyButton.addActionListener(new CopyActionListener());
        ImageIcon pasteIcon = createImageIcon("paste.gif", "Paste");
        pasteButton = new JButton(pasteIcon);
        pasteButton.setToolTipText("Paste");
        pasteButton.addActionListener(new PasteActionListener(MAX_RW_LENGTH, this));
        ImageIcon newIcon = createImageIcon("new.gif", "New Directory");
        JButton newButton = new JButton(newIcon);
        newButton.setToolTipText("New DFS Directory");
        newButton.addActionListener(new NewDirectoryActionListener());
        ImageIcon uploadIcon = createImageIcon("upload.gif", "Upload");
        JButton uploadButton = new JButton(uploadIcon);
        uploadButton.setToolTipText("Upload to DFS");
        uploadButton.addActionListener(new UploadActionListener(explorer, MAX_RW_LENGTH));
        ImageIcon downloadIcon = createImageIcon("download.gif", "Download");
        downloadButton = new JButton(downloadIcon);
        downloadButton.setToolTipText("Download from DFS");
        downloadButton.addActionListener(new DownloadActionListener(MAX_RW_LENGTH, explorer));
        ImageIcon deleteIcon = createImageIcon("delete.gif", "Delete");
        deleteButton = new JButton(deleteIcon);
        deleteButton.setToolTipText("Delete");
        deleteButton.addActionListener(new DeleteActionListener());
        displayPath = new JTextField(25);
        displayPath.addKeyListener(new KeyListener() {

            public void keyPressed(final KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    try {
                        go(displayPath.getText());
                    } catch (DFSException ex) {
                        logger.debug(ex);
                    }
                }
            }

            public void keyTyped(final KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    try {
                        go(displayPath.getText());
                    } catch (DFSException ex) {
                        logger.debug(ex);
                    }
                }
            }

            public void keyReleased(final KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    try {
                        go(displayPath.getText());
                    } catch (DFSException ex) {
                        logger.debug(ex);
                    }
                }
            }
        });
        ImageIcon goIcon = createImageIcon("go.gif", "Go");
        JButton goButton = new JButton(goIcon);
        goButton.setToolTipText("Go");
        goButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                try {
                    go(displayPath.getText());
                } catch (DFSException ex) {
                    logger.debug(ex);
                }
            }
        });
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        JScrollPane filePane = new JScrollPane(fileList);
        c = getContentPane();
        c.setBackground(Color.gray);
        c.setLayout(layout);
        c.add(filePane, BorderLayout.CENTER);
        toolbar.add(upButton);
        toolbar.add(refreshButton);
        toolbar.add(homeButton);
        toolbar.addSeparator();
        toolbar.add(propertiesButton);
        toolbar.addSeparator();
        toolbar.add(copyButton);
        toolbar.add(pasteButton);
        toolbar.addSeparator();
        toolbar.add(newButton);
        toolbar.add(uploadButton);
        toolbar.add(downloadButton);
        toolbar.add(deleteButton);
        toolbar.add(displayPath);
        displayPath.setText(current.getPath());
        toolbar.add(goButton);
        c.add(toolbar, BorderLayout.NORTH);
        setControls(false);
        pasteButton.setEnabled(false);
        setSize(650, 400);
        show();
        checkDssAvailable();
    }
