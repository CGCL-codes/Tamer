    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Sync Configuration")) {
            if (!currentConnection.isValid()) return;
            if (saveInProgress) return;
            saveInProgress = true;
            try {
                int steps = 4;
                progressDialog = new ProgressDialog(this, "Progress", true, false, 1, steps);
                progressDialog.setSize(250, 100);
                progressDialog.setLocationRelativeTo(null);
                final Runnable runnable = new Runnable() {

                    public void run() {
                        try {
                            XmlFileCreator xmlConfig = new XmlFileCreator(currentConnection);
                            String xml;
                            progressDialog.setMessage("Sendig command: " + "server stop");
                            currentConnection.doCommand("server stop");
                            progressDialog.setProgressValue(1);
                            xml = xmlConfig.getServerConfig();
                            progressDialog.setMessage("Syncing file: " + "server.xml");
                            currentConnection.uploadFile("server.xml", xml);
                            xml = xmlConfig.getFilterConfig();
                            progressDialog.setMessage("Syncing file: " + "filter.xml");
                            currentConnection.uploadFile("filter.xml", xml);
                            xml = xmlConfig.getHandlerConfig();
                            progressDialog.setMessage("Syncing file: " + "handler.xml");
                            currentConnection.uploadFile("handler.xml", xml);
                            xml = xmlConfig.getRegFilterConfig();
                            progressDialog.setMessage("Syncing file: " + "reg-filter.xml");
                            currentConnection.uploadFile("reg-filter.xml", xml);
                            progressDialog.setProgressValue(2);
                            Vector files = currentConnection.getFiles();
                            for (Enumeration f = files.elements(); f.hasMoreElements(); ) {
                                GuiFile file = (GuiFile) f.nextElement();
                                progressDialog.setMessage("Syncing file: " + file.getName());
                                currentConnection.uploadFile(file.getName(), file.getContent());
                            }
                            progressDialog.setProgressValue(3);
                            Thread.sleep(1000);
                            progressDialog.setMessage("Sendig command: " + "init");
                            currentConnection.doCommand("init");
                            progressDialog.setMessage("Sendig command: " + "server start");
                            currentConnection.doCommand("server start");
                            progressDialog.setMessage("Sendig command: " + "admin restart");
                            currentConnection.doCommand("admin restart");
                            progressDialog.setProgressValue(4);
                            Thread.sleep(1000);
                            progressDialog.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                saveInProgress = false;
                Thread thread = new Thread(runnable);
                thread.start();
                progressDialog.setVisible(true);
            } catch (Exception ex) {
                System.out.println(ex);
                saveInProgress = false;
            }
        }
        if (e.getActionCommand().equals("Connect")) {
            if (hostList.getSelectedIndex() == 0) {
                showConnectionDialog();
            } else {
                String hostString = (String) hostList.getSelectedItem();
                String host = hostString.substring(0, hostString.indexOf(":"));
                String port = hostString.substring(hostString.indexOf(":") + 1);
                connectToServer(host, port);
            }
            return;
        }
        if (e.getActionCommand().equals("Show Serverlog")) {
            if (!currentConnection.isValid()) {
                return;
            }
            if (serverLogViewer == null) {
                serverLogViewer = new LogViewer("Server Logfile");
            }
            guiBuilder.clearTreeSelection();
            String serverLogContent = currentConnection.getFileFromServer("getlog", "server.log");
            serverLogViewer.setContent(serverLogContent);
            guiBuilder.updateSplitPane(serverLogViewer);
            return;
        }
        if (e.getActionCommand().equals("Clear Serverlog")) {
            if (!currentConnection.isValid()) {
                return;
            }
            boolean ret = currentConnection.doCommand("clearlog");
            String message = currentConnection.getLastMessage();
            if (ret) {
                message = message.substring(4);
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to clear logfile!", "Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("Save Brazil Config")) {
            if (!currentConnection.isValid()) {
                return;
            }
            JFileChooser chooser;
            if (lastDirAccessed == null) {
                chooser = new JFileChooser();
            } else {
                chooser = new JFileChooser(lastDirAccessed);
            }
            chooser.setDialogTitle("Save Brazil Config");
            int ret = chooser.showSaveDialog(null);
            if (ret == JFileChooser.CANCEL_OPTION) {
                return;
            }
            lastDirAccessed = chooser.getCurrentDirectory().getPath();
            String filename = chooser.getSelectedFile().getName();
            String fullFilename = chooser.getSelectedFile().getPath();
            if (new File(fullFilename).exists()) {
                int res = JOptionPane.showConfirmDialog(this, "The file \"" + filename + "\" already exists!\n" + "Do you want to overwrite?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == 1) {
                    return;
                }
            }
            String brazilConfig = currentConnection.getFileFromServer("getBrazilConfig", null);
            Io.writeLocalFile(fullFilename, brazilConfig);
        }
        if (e.getActionCommand().equals("Server Status")) {
            String message = currentConnection.getFileFromServer("status", "");
            if (message != null) {
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, "Error while obtaining status", "Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("Stop Server")) {
            boolean ret = currentConnection.doCommand("server stop");
            String message = currentConnection.getLastMessage();
            if (ret) {
                message = message.substring(4);
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, "Error while stopping server", "Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("Start Server")) {
            currentConnection.doCommand("init");
            boolean ret = currentConnection.doCommand("server start");
            String message = currentConnection.getLastMessage();
            if (ret) {
                message = message.substring(4);
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, "Error while starting server", "Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("Shutdown Server")) {
            String warningText = "Do you really want to shutdown the server?\n" + "After shutdown the server has to be restarted manually!";
            int res = JOptionPane.showConfirmDialog(this, warningText, "Shutdown Server", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == 1) {
                return;
            }
            boolean ret = currentConnection.doCommand("shutdown");
            String message = currentConnection.getLastMessage();
            if (ret) {
                message = message.substring(4);
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, "Error while shutting down", "Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getActionCommand().equals("About")) {
            AboutDialog aboutDialog = new AboutDialog("PAW Web Filter " + PawGui.class.getPackage().getImplementationVersion(), true, false);
            aboutDialog.setLocationRelativeTo(null);
            aboutDialog.setVisible(true);
        }
        if (e.getActionCommand().equals("Help")) {
            HelpFrame helpFrame = new HelpFrame("PAW Help");
            helpFrame.setVisible(true);
        }
        if (e.getActionCommand().equals("ConnectToHost")) {
            connectDialog.dispose();
            setupConnection();
            return;
        }
        if (e.getActionCommand().equals("AuthUser")) {
            String user = authDialog.getUser();
            String pass = authDialog.getPass();
            authDialog.dispose();
            authenticate(user, pass);
            return;
        }
        if (e.getActionCommand().equals("ConnectCancel")) {
            connectDialog.dispose();
            return;
        }
        if (e.getActionCommand().equals("AuthUserCancel")) {
            authDialog.dispose();
            return;
        }
        if (e.getActionCommand().equals("Exit")) {
            saveConfig();
            System.exit(0);
        }
        if (e.getActionCommand().equals("Expand tree")) {
            guiBuilder.expandTree();
            return;
        }
        if (e.getActionCommand().equals("Collapse tree")) {
            guiBuilder.collapseTree();
            ;
            return;
        }
        if (e.getActionCommand().equals("ImportHandlersAndFilters")) {
            JFileChooser chooser;
            if (lastDirAccessed == null) {
                chooser = new JFileChooser();
            } else {
                chooser = new JFileChooser(lastDirAccessed);
            }
            chooser.setFileFilter(new XMLFileFilter());
            chooser.setDialogTitle("Import");
            int ret = chooser.showOpenDialog(null);
            if (ret == JFileChooser.CANCEL_OPTION) {
                return;
            }
            lastDirAccessed = chooser.getCurrentDirectory().getPath();
            String filename = chooser.getSelectedFile().getName();
            String fullFilename = chooser.getSelectedFile().getPath();
            String xml = Io.readLocalFile(fullFilename);
            if (xml == null) {
                JOptionPane.showMessageDialog(this, "Error importing file \"" + filename + "\"", "Imported", JOptionPane.ERROR_MESSAGE);
                return;
            }
            doImport(xml, true);
        }
        if (e.getActionCommand().equals("Export")) {
            JFileChooser chooser;
            if (lastDirAccessed == null) {
                chooser = new JFileChooser();
            } else {
                chooser = new JFileChooser(lastDirAccessed);
            }
            chooser.setFileFilter(new XMLFileFilter());
            chooser.setDialogTitle("Export");
            int ret = chooser.showSaveDialog(null);
            if (ret == JFileChooser.CANCEL_OPTION) {
                return;
            }
            lastDirAccessed = chooser.getCurrentDirectory().getPath();
            String filename = chooser.getSelectedFile().getName();
            String fullFilename = chooser.getSelectedFile().getPath();
            if (new File(fullFilename).exists()) {
                int res = JOptionPane.showConfirmDialog(this, "The file \"" + filename + "\" already exists!\n" + "Do you want to overwrite?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == 1) {
                    return;
                }
            }
            String xml = null;
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            if (node.getUserObject() instanceof GuiRegExpFilter) {
                GuiRegExpFilter regFilter = (GuiRegExpFilter) node.getUserObject();
                Vector v = new Vector();
                v.add(regFilter);
                XmlFileCreator creator = new XmlFileCreator();
                try {
                    xml = creator.getRegFilterConfig(v);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (node.getUserObject() instanceof GuiFilter) {
                GuiFilter filter = (GuiFilter) node.getUserObject();
                Vector v = new Vector();
                v.add(filter);
                XmlFileCreator creator = new XmlFileCreator();
                try {
                    xml = creator.getFilterConfig(v);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (node.getUserObject() instanceof GuiHandler) {
                GuiHandler handler = (GuiHandler) node.getUserObject();
                Vector v = new Vector();
                v.add(handler);
                XmlFileCreator creator = new XmlFileCreator();
                try {
                    xml = creator.getHandlerConfig(v);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Io.writeLocalFile(fullFilename, xml);
        }
        if (e.getActionCommand().equals("NewHandler")) {
            String xml = Io.readLocalFile("templates/Handler.xml");
            if (xml != null) {
                doImport(xml, false);
            }
        }
        if (e.getActionCommand().equals("NewFilter")) {
            String xml = Io.readLocalFile("templates/Filter.xml");
            if (xml != null) {
                doImport(xml, false);
            }
        }
        if (e.getActionCommand().equals("NewCustomFilter")) {
            String xml = Io.readLocalFile("templates/CustomFilter.xml");
            if (xml != null) {
                doImport(xml, false);
            }
        }
        if (e.getActionCommand().equals("NewReplaceFilter")) {
            String xml = Io.readLocalFile("templates/RegExpFilter-Replace.xml");
            if (xml != null) {
                doImport(xml, false);
            }
        }
        if (e.getActionCommand().equals("NewSizeFilter")) {
            String xml = Io.readLocalFile("templates/RegExpFilter-Size.xml");
            if (xml != null) {
                doImport(xml, false);
            }
        }
        if (e.getActionCommand().equals("DeleteNode")) {
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            if (node.getUserObject() instanceof GuiRegExpFilter) {
                GuiRegExpFilter userObj = (GuiRegExpFilter) node.getUserObject();
                int ret = JOptionPane.showConfirmDialog(this, "Delete RegExp Filter \"" + userObj.getName() + "\" ?", "Delete RegExp Filter", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ret == 1) {
                    return;
                }
                DefaultTreeModel model = (DefaultTreeModel) guiBuilder.getTree().getModel();
                model.removeNodeFromParent(node);
                currentConnection.removeRegExpFilter(userObj);
            } else if (node.getUserObject() instanceof GuiHandler) {
                GuiHandler userObj = (GuiHandler) node.getUserObject();
                int ret = JOptionPane.showConfirmDialog(this, "Delete Handler \"" + userObj.getName() + "\" ?", "Delete Handler", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ret == 1) {
                    return;
                }
                DefaultTreeModel model = (DefaultTreeModel) guiBuilder.getTree().getModel();
                model.removeNodeFromParent(node);
                currentConnection.removeHandler(userObj);
            } else if (node.getUserObject() instanceof GuiFilter) {
                GuiFilter userObj = (GuiFilter) node.getUserObject();
                int ret = JOptionPane.showConfirmDialog(this, "Delete Filter \"" + userObj.getName() + "\" ?", "Delete Filter", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ret == 1) {
                    return;
                }
                DefaultTreeModel model = (DefaultTreeModel) guiBuilder.getTree().getModel();
                model.removeNodeFromParent(node);
                currentConnection.removeFilter(userObj);
            }
        }
        if (e.getActionCommand().equals("SwitchNode")) {
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            if (node.getUserObject() instanceof GuiRegExpFilter) {
                GuiRegExpFilter filter = (GuiRegExpFilter) node.getUserObject();
                if (filter.isActive()) filter.setActive(false); else filter.setActive(true);
                guiBuilder.getTree().treeDidChange();
            }
            if (node.getUserObject() instanceof GuiHandler) {
                GuiHandler handler = (GuiHandler) node.getUserObject();
                if (handler.isActive()) handler.setActive(false); else handler.setActive(true);
                guiBuilder.getTree().treeDidChange();
            }
            if (node.getUserObject() instanceof GuiFilter) {
                GuiFilter filter = (GuiFilter) node.getUserObject();
                if (filter.isActive()) filter.setActive(false); else filter.setActive(true);
                guiBuilder.getTree().treeDidChange();
            }
            return;
        }
        if (e.getActionCommand().equals("MoveNodeUp")) {
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            int index = parent.getIndex(node);
            if (index > 0) {
                parent.insert(node, index - 1);
                DefaultTreeModel model = (DefaultTreeModel) guiBuilder.getTree().getModel();
                model.reload(parent);
                guiBuilder.getTree().treeDidChange();
                currentConnection.resyncHandlersAndFiltersWithTree(guiBuilder.getTree());
            }
            return;
        }
        if (e.getActionCommand().equals("MoveNodeDown")) {
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            int index = parent.getIndex(node);
            int numChildren = parent.getChildCount();
            if (index != numChildren - 1) {
                parent.insert(node, index + 1);
                DefaultTreeModel model = (DefaultTreeModel) guiBuilder.getTree().getModel();
                model.reload(parent);
                currentConnection.resyncHandlersAndFiltersWithTree(guiBuilder.getTree());
            }
            return;
        }
        if (e.getActionCommand().equals("EditFilterHandler")) {
            Object obj = guiBuilder.getTree().getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            if (node.getUserObject() instanceof GuiRegExpFilter) {
                GuiRegExpFilter regFilter = (GuiRegExpFilter) node.getUserObject();
                Vector v = new Vector();
                v.add(regFilter);
                XmlFileCreator creator = new XmlFileCreator();
                String xml = null;
                try {
                    xml = creator.getRegFilterConfig(v);
                    TextEdit textEdit = new TextEdit(xml, node, this);
                    guiBuilder.updateSplitPane(textEdit);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (node.getUserObject() instanceof GuiFilter) {
                GuiFilter filter = (GuiFilter) node.getUserObject();
                Vector v = new Vector();
                v.add(filter);
                XmlFileCreator creator = new XmlFileCreator();
                String xml = null;
                try {
                    xml = creator.getFilterConfig(v);
                    TextEdit textEdit = new TextEdit(xml, node, this);
                    guiBuilder.updateSplitPane(textEdit);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (node.getUserObject() instanceof GuiHandler) {
                GuiHandler handler = (GuiHandler) node.getUserObject();
                Vector v = new Vector();
                v.add(handler);
                XmlFileCreator creator = new XmlFileCreator();
                String xml = null;
                try {
                    xml = creator.getHandlerConfig(v);
                    TextEdit textEdit = new TextEdit(xml, node, this);
                    guiBuilder.updateSplitPane(textEdit);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
