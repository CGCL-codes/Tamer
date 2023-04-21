    public void createPartControl(final Composite parent) {
        theview = this;
        AutoDetectView.parent = parent;
        refreshname(currentfile);
        ipl = new IPartListener() {

            public void partActivated(IWorkbenchPart part) {
            }

            public void partBroughtToTop(IWorkbenchPart part) {
            }

            public void partClosed(IWorkbenchPart part) {
                if (part instanceof AutoDetectView) {
                    readyToDie(true);
                }
            }

            public void partDeactivated(IWorkbenchPart part) {
            }

            public void partOpened(IWorkbenchPart part) {
            }
        };
        getSite().getWorkbenchWindow().getPartService().addPartListener(ipl);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.verticalSpacing = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 2;
        parent.setLayout(layout);
        textBoxAtTop = new Combo(parent, SWT.FLAT);
        FormData findcomboLData = new FormData();
        findcomboLData.height = 21;
        findcomboLData.width = 342;
        findcomboLData.left = new FormAttachment(193, 1000, 0);
        findcomboLData.right = new FormAttachment(965, 1000, 0);
        findcomboLData.top = new FormAttachment(77, 1000, 0);
        findcomboLData.bottom = new FormAttachment(190, 1000, 0);
        parent.setLayoutData(findcomboLData);
        parent.setFocus();
        treeViewer = new CheckboxTreeViewer(parent, SWT.MULTI);
        treeViewer.setUseHashlookup(false);
        treeViewer.setContentProvider(ccp);
        labelProvider = new CanalTreeLabelProvider();
        treeViewer.setLabelProvider(labelProvider);
        layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        textBoxAtTop.setLayoutData(layoutData);
        createTabTopActions();
        createToolbar();
        treeViewer.setInput("root");
        PopUpCreator popUp = new PopUpCreator();
        popUp.createPopUpMenu(parent, ccp, treeViewer, this, CONSTANTS.AUTODETECTVIEW, null);
        makeTreeViewActions();
        hookDoubleClickAction();
        tree = treeViewer.getTree();
        editor = new TreeEditor(tree);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabVertical = true;
        editor.grabHorizontal = true;
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));
        ArrayList<?> list = new ArrayList();
        list = ccp.getAllChildren(ccp.getRoot1(), list);
        list = ccp.getAllChildren(ccp.getRoot2(), list);
        list = ccp.getAllChildren(ccp.getRoot3(), list);
        if (list.size() > 0) {
            reagg.setEnabled(true);
            pdfcreate.setEnabled(true);
            pdfdesc.setEnabled(true);
        }
        String feeddesc = "";
        for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
            NewCanalNode element = (NewCanalNode) iter.next();
            treeViewer.setChecked(element, element.isChecked());
            if (element.getType() == CONSTANTS.TYPE_FEED_DESCRIPTION) {
                feeddesc = element.getKey();
            } else if (element.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || element.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                if (element.getData() == null) {
                    continue;
                }
                if (element.getKey() == null) {
                    continue;
                }
                if (element.getDescription() == null) {
                    continue;
                }
                SearchCatcher.addForAutoDetect(feeddesc, element.getData(), element.getKey(), new Date(element.getDate()), element.getDescription());
            }
        }
        list.clear();
        list = null;
        treeViewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                NewCanalNode node = (NewCanalNode) event.getElement();
                if (event.getChecked()) {
                    treeViewer.setSubtreeChecked(event.getElement(), true);
                } else {
                    treeViewer.setSubtreeChecked(event.getElement(), false);
                }
                if (node.getType() == CONSTANTS.TYPE_FEED_DESCRIPTION) {
                    return;
                }
                if (node.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE) {
                    if (!node.isChecked()) {
                        NewCanalNode parent = (NewCanalNode) ccp.getParent(node);
                        NewCanalNode sibling = (NewCanalNode) ccp.getNextSibling(parent);
                        SearchCatcher.addForAutoDetect(sibling.getKey(), node.getData(), node.getKey(), new Date(node.getDate()), node.getDescription());
                    }
                    return;
                }
                if (node.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                    if (!node.isChecked()) {
                        NewCanalNode parent = (NewCanalNode) ccp.getParent(node);
                        NewCanalNode sibling = (NewCanalNode) ccp.getNextSibling(parent);
                        SearchCatcher.addForAutoDetect(sibling.getKey(), node.getData(), node.getKey(), new Date(node.getDate()), node.getDescription());
                    }
                    return;
                }
                reagg.setEnabled(true);
                pdfcreate.setEnabled(true);
                pdfdesc.setEnabled(true);
                ArrayList<?> list = new ArrayList();
                list = ccp.getAllChildren(node, list);
                for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
                    NewCanalNode element = (NewCanalNode) iter.next();
                    if (element.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || element.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                        if (!node.isChecked()) {
                            NewCanalNode parent = (NewCanalNode) ccp.getParent(element);
                            NewCanalNode sibling = (NewCanalNode) ccp.getNextSibling(parent);
                            SearchCatcher.addForAutoDetect(sibling.getKey(), element.getData(), element.getKey(), new Date(element.getDate()), element.getDescription());
                        }
                    }
                }
            }
        });
        initDND();
        textBoxAtTop.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if ((e.stateMask == SWT.CTRL || e.stateMask == SWT.COMMAND) && (e.keyCode == 'a' || e.keyCode == 'A')) textBoxAtTop.setSelection(new Point(0, textBoxAtTop.getText().length()));
            }
        });
        if (algorithmns != null) {
            textBoxAtTop.addListener(SWT.DefaultSelection, new Listener() {

                public void handleEvent(Event event) {
                    for (int a = lastSearches.size() - 1; a >= 0; a--) {
                        textBoxAtTop.add((String) lastSearches.get(a));
                    }
                    doSearch();
                }
            });
        }
    }
