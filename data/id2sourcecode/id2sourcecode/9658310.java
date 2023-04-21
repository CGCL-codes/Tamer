    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) (e.getSource());
        String command = e.getActionCommand();
        try {
            if (command.equals(MenuCreator.NEWKEY)) {
                MenuActionHandler.newKey();
            } else if (command.equals(MenuCreator.NEWATTR)) {
                MenuActionHandler.newAttribute();
            } else if (command.equals(MenuCreator.OPERATION)) {
                MenuActionHandler.newOperation();
            } else if (command.equals(MenuCreator.OPERATIONGRP)) {
                MenuActionHandler.newOperationGroup();
            } else if (command.equals(MenuCreator.RENAME)) {
                MenuActionHandler.rename();
            } else if (command.equals(MenuCreator.DELETE)) {
                AppContext.treeContext().setCut(false);
                MenuActionHandler.performDelete();
            } else if (command.equals(MenuCreator.CUT)) {
                MenuActionHandler.performCut();
            } else if (command.equals(MenuCreator.COPY)) {
                AppContext.treeContext().setCut(false);
                MenuActionHandler.performCopy();
            } else if (command.equals(MenuCreator.PASTE)) {
                MenuActionHandler.performPaste();
                AppContext.treeContext().setCut(false);
            } else if (command.equals(MenuCreator.READONLY)) {
                TableUtil.cancelCellEditing();
                AbstractButton toolbarUpdate = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
                if (!AppContext.isConnected()) {
                    menuItem.setSelected(true);
                    toolbarUpdate.setSelected(false);
                } else {
                    menuItem.setSelected(!menuItem.isSelected());
                    toolbarUpdate.setSelected(!menuItem.isSelected());
                }
                if (menuItem.isSelected()) menuItem.setIcon(DCMenuItem.selectIcon); else menuItem.setIcon(DCMenuItem.blankIcon);
                AppContext.tableContext().toggleUpdateViewMode();
                AppContext.getTree().setEditable(AppContext.appContext().isUpdateMode());
                AppContext.appContext().getStateController().updateButtonStates();
            } else if (command.equals(MenuCreator.FIND)) {
                DlgFind dlg = new DlgFind(AppContext.appContext().getCfgEditor(), "Find");
                dlg.dispose();
                dlg = null;
            } else if (command.equals(MenuCreator.SAVE)) {
                TableModelImpl model = TableUtil.getCustomTableModel();
                List<DConfigAttribute> attributes = model.getAllAttributes();
                TreePath treePath = AppContext.getTree().getSelectionPath();
                DConfig dcfgObj = TreeUtil.getDConfigObject(treePath);
                if (dcfgObj.getKey().hasChanged() && dcfgObj.getKey().isNew()) {
                    if (new DConfigKeyDao().save(dcfgObj.getKey())) {
                        for (int i = 0; i < attributes.size(); i++) {
                            DConfigAttribute attribute = attributes.get(i);
                            attribute.setKeyId(dcfgObj.getKey().getId());
                        }
                    }
                }
                if (new DConfigAttributeDao().save(dcfgObj, attributes)) {
                    model.populateTable(dcfgObj.getAttributesFromDb());
                    dcfgObj.setAttributes(attributes);
                    AppContext.appContext().getStateController().updateButtonStates();
                } else {
                    DialogUtil.showError("Save Attributes Failed", "Sorry, an error occurred while saving attributes to database. Please try again later.");
                }
            } else if (command.equals(MenuCreator.FINDNEXT)) {
                FindUtil.findNext();
            } else if (command.equals(MenuCreator.FINDPREVIOUS)) {
                FindUtil.findPrevious();
            } else if (command.equals(MenuCreator.COPYKEYNAME)) {
                setClipboard(AppContext.treeContext().getUIKey());
            } else if (command.equals(MenuCreator.STRONGTYPEDEDITOR)) {
                menuItem.setSelected(!menuItem.isSelected());
                if (menuItem.isSelected()) menuItem.setIcon(DCMenuItem.selectIcon); else menuItem.setIcon(DCMenuItem.blankIcon);
            } else if (command.equals(MenuCreator.IMPORT)) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showOpenDialog(AppContext.appContext().getCfgEditor());
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String importFile = c.getSelectedFile().getPath();
                    Import importer = new Import(importFile);
                    if (importer.doImport()) {
                        TreePopulator.populateTree();
                        DialogUtil.showInfo("Import", "Data import succeeded.");
                    } else DialogUtil.showError("Import", "Sorry, an error occurred while importinging data, check log file for details.");
                }
            } else if (command.equals(MenuCreator.EXPORT)) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showSaveDialog(AppContext.appContext().getCfgEditor());
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String exportFile = c.getSelectedFile().getPath();
                    boolean bContinue = true;
                    File file = new File(exportFile);
                    if (file.exists()) {
                        int iRet = DialogUtil.showYesNoConfirm("Confirm File Overwritten", "The file you selected already exists, do you want to overwrite it?");
                        if (iRet != JOptionPane.YES_OPTION) {
                            bContinue = false;
                        }
                    }
                    if (bContinue) {
                        Export export = new Export(exportFile, AppContext.treeContext().getSelectedNode());
                        if (export.doExport()) {
                            DialogUtil.showInfo("Export", "Data export succeeded.");
                        } else {
                            DialogUtil.showError("Export", "Sorry, an error occurred while exporting data, check log file for details.");
                        }
                    }
                }
            } else if (command.equals(MenuCreator.EXPAND_COLLAPSE)) {
                JTree tree = AppContext.getTree();
                TreePath treePath = tree.getSelectionPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                if (tree.isCollapsed(treePath)) {
                    tree.expandPath(treePath);
                } else {
                    tree.collapsePath(treePath);
                }
            } else if (command.equals(MenuCreator.EXPAND_COLLAPSE_ALL)) {
                JTree tree = AppContext.getTree();
                TreePath treePath = tree.getSelectionPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                if (tree.isCollapsed(treePath)) {
                    TreeUtil.expandAll(tree, treePath, true);
                } else {
                    TreeUtil.expandAll(tree, treePath, false);
                }
            } else if (command.equals(MenuCreator.RRFRESH)) {
                TreeUtil.refreshSelectedNode();
            } else if (command.equals(MenuCreator.DBCONNECTION)) {
                DlgDbConnection dlg = new DlgDbConnection(AppContext.appContext().getCfgEditor(), true);
                dlg.setVisible(true);
                dlg.dispose();
                dlg = null;
            } else if (command.equals(MenuCreator.DRIVERMANAGER)) {
                DlgDriverManager dlg = new DlgDriverManager(AppContext.appContext().getCfgEditor(), true);
                dlg.setVisible(true);
                dlg.dispose();
                dlg = null;
            } else if (command.equals(MenuCreator.CONNECT)) {
                boolean openConnectionWizard = false;
                if (AppContext.isConnected()) {
                    openConnectionWizard = true;
                } else {
                    ConnectionInfo connectionInfo = ConnectionInfoUtil.getDefaultConnectionInfo();
                    if (connectionInfo != null) {
                        ConnectionInfo ciOld = DataSourceManager.getConnectionInfo();
                        if (!connectionInfo.equals(ciOld)) {
                            DataSourceManager.setConnectionInfo(connectionInfo);
                            if (DbUtil.isDbConfigured()) {
                                if (TreePopulator.populateTree()) {
                                    AppUtil.setTitle(connectionInfo.getAlias());
                                    AppContext.setConnected(true);
                                    AbstractButton toolbarUpdate = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
                                    if (toolbarUpdate.isSelected()) toolbarUpdate.doClick();
                                }
                            } else {
                                log.error("Cannot connect to the specified db since dconfig tables are not configured properly.");
                                DataSourceManager.setConnectionInfo(ciOld);
                                openConnectionWizard = true;
                            }
                        }
                    } else {
                        openConnectionWizard = true;
                    }
                }
                if (openConnectionWizard) {
                    DlgDbConnection dlg = new DlgDbConnection(AppContext.appContext().getCfgEditor(), true);
                    dlg.setVisible(true);
                    dlg.dispose();
                    dlg = null;
                }
                if (AppContext.isConnected()) MenuUtil.setEnable(MenuCreator.DISCONNECT, true);
            } else if (command.equals(MenuCreator.DISCONNECT)) {
                AppContext.setDisconnectAction(true);
                AppUtil.disconnect();
                AppContext.setConnected(false);
                AbstractButton toolbarUpdate = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
                AbstractButton toolbarDisconnect = ToolbarUtil.findToolBarButton(MenuCreator.DISCONNECT);
                TableModelImpl model = TableUtil.getCustomTableModel();
                model.clear();
                model.hasChanged();
                toolbarUpdate.doClick();
                DataSourceManager.setConnectionInfo(null);
                AppContext.setDisconnectAction(false);
                MenuUtil.setEnable(MenuCreator.DISCONNECT, false);
            } else if (command.equals(MenuCreator.ABOUT)) {
                DlgAbout dlg = new DlgAbout(AppContext.appContext().getCfgEditor(), true);
                dlg.setVisible(true);
                dlg.dispose();
                dlg = null;
            } else if (command.equals(MenuCreator.EXIT)) {
                if (AppState.isDemo()) DemoDbManager.shutdown();
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
