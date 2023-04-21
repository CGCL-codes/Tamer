    public RequirementTool() {
        super();
        ToolTipManager.sharedInstance().registerComponent(requirementTree);
        requirementTree.addTreeExpansionListener(new TreeExpansionListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
            }

            public void treeExpanded(TreeExpansionEvent event) {
                final TreeNode node = (TreeNode) event.getPath().getLastPathComponent();
                resetExpansionHandles(node);
            }
        });
        UnloadedFileDisposer unloadedFileDisposer = new UnloadedFileDisposer();
        repositoryManager.addChangeListener(unloadedFileDisposer);
        addChainedFrameClosingListener(unloadedFileDisposer);
        editingStateManager.addAction(runTestAction);
        editingStateManager.addAction(openTestSessionAction);
        UserRoster.instance.setAvailableRoles(Roles.values());
        UserRoster.OWNER = Roles.OWNER;
        requirementTreeModel.setUserMonitor(new UserMonitor(this));
        testsTable = createTestTable();
        allTestsExecutionAction = new AllTestsExecutionAction(testRunner, getCurrentTestListModel());
        testExportToPdfAction = new ExportTestToPdfAction(repositoryManager);
        testExportToDocAction = new ExportTestToDocAction(repositoryManager);
        setJMenuBar(createJMenuBar());
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(createToolBar(), BorderLayout.NORTH);
        versionPanel = new VersionPanel(allRequirementsModel, requirementFactory, this, editingStateManager, mementos);
        requirementTreeModel.addTreeModelListener(versionPanel);
        tabbedPane = createContentPane();
        allTestsExecutionAction.setTabToShow(tabbedPane, TAB_TESTS);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(statusPane, BorderLayout.SOUTH);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        centerOnScreen(new Dimension(Math.min(screenSize.width, 1000), Math.min(screenSize.height, 830)));
        addChainedFrameClosingListener(requirementTreeModel);
        domainPanel.linkToFrame(this);
        requirementPanel.linkToFrame(this);
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                setStatusMessage("", false);
            }
        });
        buildTestMenu();
        requirementTreeModel.reset();
        if (IssueListModel.hasIssueTrackerServices()) {
            connectToJira();
        }
        setCloseOperator(EXIT_CLOSE_OPERATOR);
        repositoryManager.addChangeListener(statusPane);
        repositoryManager.addChangeListener(editingStateManager);
        new FileDropAdapter(this, this);
        final Autosaver autosaver = new Autosaver(this, 60 * Preferences.instance.getAutosaveDelay(), TimeUnit.SECONDS);
        mementos.addMementoEventListener(autosaver);
        addChainedFrameClosingListener(new IChainedFrameClosingListener() {

            public void askPermissionToClose(WindowEvent _event) {
                autosaver.terminate();
                try {
                    OpenOfficeManager.dispose();
                } catch (OpenOfficeException e) {
                    LOGGER.log(Level.WARNING, "Failed to dispose " + OpenOfficeManager.class.getSimpleName(), e);
                }
            }
        });
        checkOOoVersion();
    }
