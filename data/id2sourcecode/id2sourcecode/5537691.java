    public FlatFileSetFrame() {
        super("Specify Your Flat File Data");
        this.lastselectedsourcerow = -1;
        this.SHOULD_PULL_FLATFILES_FROM_REPOSITORY = false;
        try {
            Class transferAgentClass = this.getStorageTransferAgentClass();
            if (transferAgentClass == null) {
                throw new RuntimeException("Transfer agent class can not be null.");
            }
            Class[] parameterTypes = new Class[] { RepositoryStorage.class };
            Constructor constr = transferAgentClass.getConstructor(parameterTypes);
            Object[] actualValues = new Object[] { this };
            this.transferAgent = (RepositoryStorageTransferAgent) constr.newInstance(actualValues);
        } catch (Exception err) {
            throw new RuntimeException("Unable to instantiate transfer agent.", err);
        }
        this.fmtlistener = new FormatTableModelListener();
        this.waitIndicator = new JLabel("X");
        this.waitIndicator.setHorizontalAlignment(JLabel.CENTER);
        this.waitIndicator.setPreferredSize(new Dimension(25, 25));
        this.waitIndicator.setMaximumSize(new Dimension(25, 25));
        this.waitIndicator.setMinimumSize(new Dimension(25, 25));
        this.NoCallbackChangeMode = false;
        this.setSize(new Dimension(1000, 550));
        this.setLayout(new GridLayout(1, 1));
        this.Config = new FlatFileToolsConfig();
        this.Config.initialize();
        this.connectionHandler = new RepositoryConnectionHandler(this.Config);
        this.Connection = (FlatFileStorageConnectivity) this.connectionHandler.getConnection("default");
        this.Prefs = new FlatFileToolsPrefs();
        this.Prefs.initialize();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatted_date = formatter.format(new Date());
        this.createdOnText = new JTextField(formatted_date);
        this.createdByText = new JTextField(this.Prefs.getConfigValue("createdby"));
        this.reposListeners = new Vector();
        this.removeFormatButton = new JButton("Remove");
        this.previewPanel = new DataSetPanel(new DataSet());
        this.previewPanel.setEditable(false);
        this.previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Preview:"));
        this.chooser = new JFileChooser();
        this.chooser.setMultiSelectionEnabled(true);
        this.clearButton = new JButton("Clear");
        this.clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                NoCallbackChangeMode = true;
                backingStorage = null;
                formatmodel.setFormat(null);
                preheaderlines.setValue(0);
                postheaderlines.setValue(0);
                commentTextArea.setText("");
                flatfileCommentArea.setText("");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formatted_date = formatter.format(new Date());
                createdOnText = new JTextField(formatted_date);
                createdByText = new JTextField(Prefs.getConfigValue("createdby"));
                fieldDelimiter.setText("\\t");
                recordDelimiter.setText("\\n");
                hasHeaderLineBox.setSelected(false);
                enabledRadio.setSelected(true);
                nicknameText.setText("");
                flatfilenicknameText.setText("");
                sortProtocols.setSelectedIndex(0);
                singleFormatText.setText("%s");
                repeatFormatNumber.setValue(new Integer(1));
                sourcemodel.clearFlatFiles();
                previewPanel.setDataSet(new DataSet());
                NoCallbackChangeMode = false;
            }
        });
        this.enabledRadio = new JRadioButton("Enabled:");
        this.enabledRadio.setSelected(true);
        this.enabledRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                if (!enabledRadio.isSelected()) {
                    int t = JOptionPane.showConfirmDialog(null, "Note, disabling a storage deprecates it and schedules it for deletion.  Disable this storage?", "Deprecate storage?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (t != JOptionPane.YES_OPTION) {
                        enabledRadio.setEnabled(false);
                        enabledRadio.setSelected(true);
                        enabledRadio.setEnabled(true);
                    }
                }
            }
        });
        this.flatFileEnabledRadio = new JRadioButton("Enabled:");
        this.flatFileEnabledRadio.setSelected(true);
        this.flatFileEnabledRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                if (!flatFileEnabledRadio.isSelected()) {
                    int t = JOptionPane.showConfirmDialog(null, "Note, disabling a storage deprecates it and schedules it for deletion.  Disable this storage?", "Deprecate storage?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (t != JOptionPane.YES_OPTION) {
                        flatFileEnabledRadio.setEnabled(false);
                        flatFileEnabledRadio.setSelected(true);
                        flatFileEnabledRadio.setEnabled(true);
                    }
                }
            }
        });
        this.editPrefsButton = new JButton("Preferences...");
        this.editPrefsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(true);
            }
        });
        this.flatfileCommentArea = new JTextArea(2, 16);
        this.flatfileCommentArea.setToolTipText("A detailed description of this file source.");
        this.flatfileCommentArea.setText(" ");
        Document doc = this.flatfileCommentArea.getDocument();
        doc.addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }

            public void insertUpdate(DocumentEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }

            public void removeUpdate(DocumentEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        this.commentTextArea = new JTextArea(2, 16);
        this.commentTextArea.setToolTipText("A detailed (possibly formatted) description including guidance to future developers of this set.");
        this.commentTextArea.setText(" ");
        this.iconServer = new IconServer();
        this.iconServer.setConfigFile(this.Prefs.getConfigValue("default", "iconmapfile"));
        this.findSet = new FlatFileFindNameDialog(Config, iconServer);
        this.findSet.setSearchClass(FlatFileSetStorage.class);
        this.nicknameText = new IconifiedDomainNameTextField(findSet, this.iconServer);
        this.findFile = new FlatFileFindNameDialog(Config, iconServer);
        this.findFile.setSearchClass(FlatFileStorage.class);
        this.findFile.addOkListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String selectedFile = findFile.getSelectedName();
                if (selectedFile != null) {
                    FlatFileStorage file = (FlatFileStorage) Connection.getStorage(selectedFile);
                    FlatFileDOM dom = new FlatFileDOM();
                    dom.transferStorage(file);
                    sourcemodel.addFlatFile(dom);
                } else {
                }
                if (anyNonEmptySources()) {
                    removeSources.setEnabled(true);
                    allowFormatParsing(true);
                } else {
                    removeSources.setEnabled(false);
                    allowFormatParsing(false);
                }
                fileselector.getSelectionModel().setSelectionInterval(0, 0);
            }
        });
        this.findSet.addOkListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String selectedSet = findSet.getSelectedName();
                if (selectedSet != null) {
                    FlatFileSetStorage set = (FlatFileSetStorage) Connection.getStorage(selectedSet);
                    FlatFileSetFrame.this.transferStorage(set);
                } else {
                }
                int N = fileSetSize();
                for (int k = 0; k < N; k++) {
                    FlatFileDOM dom = sourcemodel.getFlatFileDOM(k);
                    String storagenickname = dom.getNickname();
                    FlatFileStorage src = (FlatFileStorage) Connection.getStorage(storagenickname);
                    if (src != null) {
                        dom.transferStorage(src);
                    }
                }
                if (anyNonEmptySources()) {
                    removeSources.setEnabled(true);
                    allowFormatParsing(true);
                } else {
                    removeSources.setEnabled(false);
                    allowFormatParsing(false);
                }
                fileselector.getSelectionModel().setSelectionInterval(0, 0);
            }
        });
        URL url = this.getClass().getResource("images/file_write_icon.png");
        ImageIcon icon = new ImageIcon(url);
        Image im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.uploadflatfileButton = new JButton(icon);
        this.uploadflatfileButton.setToolTipText("Upload this single file source specification to the repository.");
        this.flatfilenicknameText = new IconifiedDomainNameTextField(findFile, this.iconServer);
        this.flatfilenicknameText.addDomainNameListener(new DomainNameListener() {

            public void actionPerformed(DomainNameEvent ev) {
                IconifiedDomainNameTextField tf = (IconifiedDomainNameTextField) ev.getSource();
                int sr = fileselector.getSelectedRow();
                if (sr >= 0) {
                    FlatFileDOM dom = sourcemodel.getFlatFileDOM(sr);
                    if (dom != null) {
                        dom.setNickname(tf.getText());
                    }
                }
            }
        });
        this.nicknameText.setPreferredSize(new Dimension(200, 25));
        this.nicknameText.setText(this.Prefs.getConfigValue("default", "domainname") + ".");
        this.nicknameText.setNameTextToolTipText("Right click to search the database.");
        url = this.getClass().getResource("images/file_write_icon.png");
        icon = new ImageIcon(url);
        im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.uploadButton = new JButton(icon);
        this.uploadButton.setToolTipText("Uploads entire set configuration to repository.");
        this.uploadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                boolean do_transfer = false;
                try {
                    String expname = getNickname();
                    int split = expname.lastIndexOf('.');
                    String domain = "";
                    String name = "";
                    String usersdomain = Prefs.getConfigValue("default", "domainname");
                    if (split > 0) {
                        domain = expname.substring(0, split);
                        name = expname.substring(split + 1, expname.length());
                    } else {
                        name = expname;
                    }
                    name = name.trim();
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(null, "Cowardly refusing to upload with an empty buffer name...");
                        return;
                    }
                    if (!domain.equals(usersdomain)) {
                        int s = JOptionPane.showConfirmDialog(null, "If you are not the original author, you may wish to switch the current domain name " + domain + " to \nyour domain name " + usersdomain + ".  Would you like to do this?\n (If you'll be using this domain often, you may want to set it in your preferences.)", "Potential WWW name-space clash!", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (s == JOptionPane.YES_OPTION) {
                            setNickname(usersdomain + "." + name);
                            do_transfer = executeTransfer();
                        }
                        if (s == JOptionPane.NO_OPTION) {
                            do_transfer = executeTransfer();
                        }
                    } else {
                        do_transfer = executeTransfer();
                    }
                } catch (Exception err) {
                    throw new RuntimeException("Problem uploading storage.", err);
                }
                if (do_transfer) {
                    int s = JOptionPane.showConfirmDialog(null, "At this time you may also upload the individual flat file storage specifications supporting this set.\nWould you like to do this? ", "Create/Upload Flat File Specs Too?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (s == JOptionPane.YES_OPTION) {
                        boolean yes_to_all = false;
                        for (int m = 0; m < sourcemodel.getRowCount(); m++) {
                            FlatFileDOM dom = sourcemodel.getFlatFileDOM(m);
                            String nn = dom.getNickname();
                            if (nn != null) {
                                nn = nn.trim();
                            }
                            if ((nn != null) && (nn.length() != 0)) {
                                if (Connection.storageExists(nn)) {
                                    int t = -1;
                                    if (!yes_to_all) {
                                        Object[] options = { "Yes", "No", "Yes to all" };
                                        t = JOptionPane.showOptionDialog(FlatFileSetFrame.this, "Storage " + nn + " already exists.  Would you like to overwrite?", "Overwrite Flat File Spec?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                                    } else {
                                        t = 0;
                                    }
                                    if (t == 2) {
                                        yes_to_all = true;
                                        t = 0;
                                    }
                                    if (t == 0) {
                                        System.out.println("Overwriting existing storage " + nn);
                                        FlatFileStorage target = (FlatFileStorage) Connection.getStorage(nn);
                                        if (target == null) {
                                            throw new RuntimeException("Storage " + nn + " was indicated to exist but could not be retrieved.");
                                        }
                                        target.transferStorage(dom);
                                    }
                                } else {
                                    Connection.createStorage(FlatFileStorage.class, nn);
                                    FlatFileStorage target = (FlatFileStorage) Connection.getStorage(nn);
                                    target.transferStorage(dom);
                                }
                            }
                        }
                    }
                }
                setEditable(true);
            }
        });
        this.repositoryView = new JButton("default");
        this.repositoryView.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                setRepository(repositoryView.getText());
                repositoryEditor.setVisible(true);
            }
        });
        String[] srtprotocol = this.Config.getSplitConfigValue(this.repositoryView.getText(), "sortprotocol");
        this.sortProtocols = new JComboBox(srtprotocol);
        this.sortProtocols.setPrototypeDisplayValue("WWWWWWWWWW");
        this.prefsEditor = new PrefsConfigFrame(this.Prefs);
        this.prefsEditor.setVisible(false);
        this.prefsEditor.addCloseListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(false);
            }
        });
        this.prefsEditor.addSelectListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(false);
            }
        });
        this.repositoryEditor = new ReposConfigFrame(this.Config);
        this.repositoryEditor.setVisible(false);
        this.repositoryEditor.addSelectListener(new SelectListener());
        this.repositoryEditor.addCloseListener(new CloseListener());
        url = this.getClass().getResource("images/file_save_icon.png");
        icon = new ImageIcon(url);
        im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.addSources = new JButton(icon);
        this.addSources.setToolTipText("Add a local flat file to the set.");
        url = this.getClass().getResource("images/repository_icon.png");
        icon = new ImageIcon(url);
        im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.addSourcesFromStorage = new JButton(icon);
        this.addSourcesFromStorage.setToolTipText("Add a previously configured flat file from the repository.");
        this.addSourcesFromURL = new JButton("WWW");
        this.addSourcesFromURL.setToolTipText("Add a (possibly remote) flat file by giving its URL.");
        this.removeSources = new JButton("Remove data");
        this.removeSources.setEnabled(false);
        this.preview = new JButton("Preview");
        this.leastcolumn = new JSpinner();
        this.columns2show = new JSpinner();
        this.leastrow = new JSpinner();
        this.rows2show = new JSpinner();
        Object[] columnNames = new Object[2];
        columnNames[0] = "File Source";
        columnNames[1] = "URL";
        int rowCount = 10;
        this.sourcemodel = new FileSourceTableModel();
        this.fileselector = new JTable(this.sourcemodel);
        TableColumn col = this.fileselector.getColumnModel().getColumn(1);
        int width = 50;
        col.setPreferredWidth(width);
        col.setMinWidth(width);
        col.setMaxWidth(width);
        ListSelectionModel selectModel = this.fileselector.getSelectionModel();
        selectModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                System.out.println("Number of selected row is:" + selectedRow);
                if (selectedRow >= 0) {
                    int[] multselected = fileselector.getSelectedRows();
                    if (multselected.length == 1) {
                        lastselectedsourcerow = selectedRow;
                        FlatFileDOM dom = sourcemodel.getFlatFileDOM(selectedRow);
                        showDetailsFor(dom);
                    }
                }
            }
        });
        JScrollPane jsp = new JScrollPane(this.fileselector);
        jsp.setPreferredSize(new Dimension(100, 100));
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.fileselector.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse was pressed.");
                if (e.isPopupTrigger()) {
                    System.out.println("Popup triggered.");
                    int sr = fileselector.getSelectedRow();
                    if (sr >= 0) {
                        String fn = (String) sourcemodel.getValueAt(sr, 0);
                        System.out.println("FILE is " + fn);
                        try {
                            Process p = new ProcessBuilder("firefox", fn).start();
                        } catch (Exception err) {
                            throw new RuntimeException("Unable to open web browser.", err);
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
        Box controlBox = Box.createHorizontalBox();
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(this.addSources);
        buttonPanel.add(this.addSourcesFromURL);
        buttonPanel.add(this.addSourcesFromStorage);
        buttonPanel.setPreferredSize(new Dimension(250, 30));
        buttonPanel.setMaximumSize(new Dimension(250, 30));
        buttonPanel.setMinimumSize(new Dimension(250, 30));
        controlBox.add(buttonPanel);
        controlBox.add(this.removeSources);
        controlBox.add(this.preview);
        controlBox.add(this.waitIndicator);
        Box scrollBox = Box.createVerticalBox();
        Box commentBox = Box.createVerticalBox();
        commentBox.add(new JScrollPane(commentTextArea));
        commentBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File set description:"));
        scrollBox.add(commentBox);
        scrollBox.add(controlBox);
        scrollBox.add(jsp);
        JLabel label = new JLabel("Sort protocol:");
        Box protoBox = Box.createHorizontalBox();
        protoBox.add(label);
        protoBox.add(this.sortProtocols);
        protoBox.setPreferredSize(new Dimension(500, 50));
        protoBox.setMaximumSize(new Dimension(500, 50));
        scrollBox.add(protoBox);
        Box srcbox = Box.createHorizontalBox();
        srcbox.add(scrollBox);
        srcbox.add(Box.createVerticalGlue());
        Box detailsbox = Box.createVerticalBox();
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(1, 2));
        detailsPanel.setBackground(Color.LIGHT_GRAY);
        Box detailsBox = Box.createVerticalBox();
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Selected file source:"));
        Box jointBox = Box.createHorizontalBox();
        jointBox.add(this.uploadflatfileButton);
        jointBox.add(this.flatfilenicknameText);
        jointBox.add(this.flatFileEnabledRadio);
        detailsBox.add(jointBox);
        detailsBox.add(Box.createVerticalGlue());
        Box cBox = Box.createHorizontalBox();
        cBox.add(new JLabel("Comment:"));
        JScrollPane csp = new JScrollPane(this.flatfileCommentArea);
        csp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cBox.add(csp);
        cBox.setPreferredSize(new Dimension(100, 200));
        detailsBox.add(cBox);
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Pre-Header Lines:");
        this.preheaderlines = new JSpinner();
        jointBox.add(label);
        jointBox.add(this.preheaderlines);
        detailsBox.add(jointBox);
        this.preheaderlines.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Has Header Line:");
        this.hasHeaderLineBox = new JCheckBox();
        jointBox.add(label);
        jointBox.add(this.hasHeaderLineBox);
        detailsBox.add(jointBox);
        this.hasHeaderLineBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Post-Header Lines:");
        this.postheaderlines = new JSpinner();
        jointBox.add(label);
        jointBox.add(this.postheaderlines);
        detailsBox.add(jointBox);
        this.postheaderlines.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Format:");
        jointBox.add(label);
        this.singleFormatText = new JTextField("%s");
        jointBox.add(this.singleFormatText);
        jointBox.add(new JLabel("Repeat"));
        this.repeatFormatNumber = new JSpinner();
        this.repeatFormatNumber.setValue(new Integer(1));
        jointBox.add(this.repeatFormatNumber);
        this.addFormatButton = new JButton("Add");
        jointBox.add(this.addFormatButton);
        this.removeFormatButton = new JButton("Remove");
        jointBox.add(this.removeFormatButton);
        detailsBox.add(jointBox);
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Column Format:");
        this.formatmodel = new FormatTableModel();
        this.formatTable = new JTable(this.formatmodel);
        this.formatmodel.addTableModelListener(this.fmtlistener);
        JTable hdrTable = this.formatTable.getTableHeader().getTable();
        this.formatTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane fsp = new JScrollPane(this.formatTable);
        fsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        fsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        fsp.setPreferredSize(new Dimension(300, 50));
        fsp.setMaximumSize(new Dimension(300, 50));
        fsp.setMinimumSize(new Dimension(300, 50));
        jointBox.add(label);
        jointBox.add(fsp);
        detailsBox.add(jointBox);
        detailsBox.add(Box.createVerticalGlue());
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Field Delimiter:");
        this.fieldDelimiter = new JTextField("\\t");
        this.fieldDelimiter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        jointBox.add(label);
        jointBox.add(this.fieldDelimiter);
        this.inferButton = new JButton("Infer");
        this.inferButton.setEnabled(false);
        jointBox.add(this.inferButton);
        detailsBox.add(jointBox);
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Record Delimiter:");
        this.recordDelimiter = new JTextField("\\n");
        this.recordDelimiter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        this.copyFormatingToSelected = new JButton("Copy format");
        this.copyFormatingToSelected.setToolTipText("Copies this formating specification to the selected sources in the set.");
        this.copyFormatingToSelected.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int selectedRow = fileselector.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = lastselectedsourcerow;
                    FlatFileDOM srcDOM = sourcemodel.getFlatFileDOM(selectedRow);
                    if (srcDOM == null) {
                        return;
                    }
                    int[] multselected = fileselector.getSelectedRows();
                    if (multselected.length > 1) {
                        for (int j = 0; j < multselected.length; j++) {
                            if (j != selectedRow) {
                                FlatFileDOM destDOM = (FlatFileDOM) sourcemodel.getFlatFileDOM(multselected[j]);
                                destDOM.setSameFormatAs(srcDOM);
                            }
                        }
                    }
                }
            }
        });
        jointBox.add(label);
        jointBox.add(this.recordDelimiter);
        detailsBox.add(jointBox);
        detailsBox.add(this.copyFormatingToSelected);
        detailsBox.add(Box.createVerticalGlue());
        detailsBox.add(Box.createVerticalGlue());
        detailsPanel.add(detailsBox);
        detailsPanel.add(previewPanel);
        this.addFormatButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String fmt2rep = singleFormatText.getText();
                Integer rep = (Integer) repeatFormatNumber.getValue();
                Vector fmtparts = formatmodel.getFormatParts();
                int selectedCol = formatTable.getSelectedColumn();
                if (selectedCol < 0) {
                    selectedCol = formatTable.getColumnCount() - 1;
                }
                for (int r = 1; r <= rep.intValue(); r++) {
                    fmtparts.insertElementAt(fmt2rep, selectedCol);
                }
                formatmodel.setFormatParts(fmtparts);
                updateFormatTable();
                int selectedRow = fileselector.getSelectedRow();
                if ((selectedRow < sourcemodel.getRowCount()) && (selectedRow >= 0)) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        this.removeFormatButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int selectedCol = formatTable.getSelectedColumn();
                if (selectedCol < 0) {
                    return;
                }
                Vector parts = formatmodel.getFormatParts();
                if (parts.size() == 1) {
                    throw new RuntimeException("At least one format column is required.");
                }
                parts.removeElementAt(selectedCol);
                formatmodel.setFormatParts(parts);
                updateFormatTable();
                int selectedRow = fileselector.getSelectedRow();
                if ((selectedRow < sourcemodel.getRowCount()) && (selectedRow >= 0)) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        this.inferButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int row = fileselector.getSelectedRow();
                int col = 0;
                String filename = (String) sourcemodel.getValueAt(0, 0);
                Boolean isURL = (Boolean) sourcemodel.getValueAt(0, 1);
                BufferedReader br = null;
                File file = null;
                DataInputStream in = null;
                if (isURL.booleanValue()) {
                    try {
                        URL url2goto = new URL(filename);
                        in = new DataInputStream(url2goto.openStream());
                    } catch (Exception err) {
                        throw new RuntimeException("Problem constructing URI for " + filename + ".", err);
                    }
                } else {
                    file = new File(filename);
                    if (!file.exists()) {
                        throw new RuntimeException("The file named '" + filename + "' does not exist.");
                    }
                    FileInputStream fstream = null;
                    try {
                        fstream = new FileInputStream(filename);
                        in = new DataInputStream(fstream);
                    } catch (Exception err) {
                        throw new RuntimeException("Problem creating FileInputStream for " + filename + ".", err);
                    }
                }
                br = new BufferedReader(new InputStreamReader(in));
                JTable hdrTable = formatTable.getTableHeader().getTable();
                try {
                    String strLine;
                    int line = 0;
                    int ignorePreHdrLines = ((Integer) preheaderlines.getValue()).intValue();
                    int ignorePostHdrLines = ((Integer) postheaderlines.getValue()).intValue();
                    int numhdr = 0;
                    boolean hasHeaderLine = false;
                    if (hasHeaderLineBox.isSelected()) {
                        hasHeaderLine = true;
                    }
                    if (hasHeaderLine) {
                        numhdr = 1;
                    }
                    String FD = fieldDelimiter.getText();
                    while ((strLine = br.readLine()) != null) {
                        if (line <= (ignorePreHdrLines + numhdr + ignorePostHdrLines)) {
                        } else {
                            String[] putative_cols = strLine.split(FD);
                            String FMT = "";
                            while (formatTable.getColumnCount() > putative_cols.length) {
                                TableColumn tcol = formatTable.getColumnModel().getColumn(0);
                                formatTable.removeColumn(tcol);
                            }
                            for (int i = 0; i < putative_cols.length; i++) {
                                String fmt = "";
                                try {
                                    Double dummy = new Double(putative_cols[i]);
                                    fmt = "%f";
                                } catch (Exception err) {
                                    fmt = "%s";
                                }
                                FMT = FMT + fmt;
                            }
                            formatmodel.setFormat(FMT);
                            break;
                        }
                        line++;
                    }
                    in.close();
                } catch (Exception err) {
                    throw new RuntimeException("Problem reading single line from file.", err);
                }
                for (int j = 0; j < formatTable.getColumnCount(); j++) {
                    hdrTable.getColumnModel().getColumn(j).setHeaderValue("" + (j + 1));
                }
                formatTable.repaint();
                int selectedRow = fileselector.getSelectedRow();
                if ((selectedRow < sourcemodel.getRowCount()) && (selectedRow >= 0)) {
                    updateDetailsFor(selectedRow);
                }
            }
        });
        Box topbox = Box.createHorizontalBox();
        topbox.add(srcbox);
        topbox.add(detailsPanel);
        Box mainbox = Box.createVerticalBox();
        Box maintenanceBox = Box.createHorizontalBox();
        Box setBox = Box.createHorizontalBox();
        setBox.add(this.clearButton);
        setBox.add(this.editPrefsButton);
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Created On:");
        jointBox.add(label);
        this.createdOnText.setPreferredSize(new Dimension(50, 25));
        jointBox.add(this.createdOnText);
        setBox.add(jointBox);
        jointBox = Box.createHorizontalBox();
        label = new JLabel("Created By:");
        jointBox.add(label);
        this.createdByText.setPreferredSize(new Dimension(50, 25));
        jointBox.add(this.createdByText);
        setBox.add(jointBox);
        setBox.add(this.uploadButton);
        setBox.add(this.repositoryView);
        setBox.add(this.nicknameText);
        setBox.add(this.enabledRadio);
        mainbox.add(setBox);
        mainbox.add(maintenanceBox);
        mainbox.add(topbox);
        mainbox.add(previewPanel);
        this.add(mainbox);
        this.removeSources.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int[] rows = fileselector.getSelectedRows();
                for (int k = 0; k < rows.length; k++) {
                    removeFlatFile(k);
                }
                if (anyNonEmptySources()) {
                    removeSources.setEnabled(true);
                    allowFormatParsing(true);
                } else {
                    removeSources.setEnabled(false);
                    allowFormatParsing(false);
                }
            }
        });
        this.addSources.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int option = chooser.showOpenDialog(null);
                File[] files = null;
                if (option == JFileChooser.APPROVE_OPTION) {
                    files = chooser.getSelectedFiles();
                    int selectedRow = fileselector.getSelectedRow();
                    boolean appendFiles = false;
                    if (selectedRow < 0) {
                        appendFiles = true;
                        selectedRow = fileselector.getRowCount() - 1;
                    }
                    String currentRepos = repositoryView.getText();
                    for (int j = 0; j < files.length; j++) {
                        String fn = files[j].getAbsolutePath();
                        String proposed_nickname = getProposedNickname(currentRepos, fn, false);
                        FlatFileDOM dom = new FlatFileDOM();
                        dom.setNickname(proposed_nickname);
                        dom.setFilename(fn);
                        dom.setEnabled("true");
                        if (appendFiles) {
                            addFlatFile(dom);
                        } else {
                            addFlatFile(selectedRow + j, dom);
                        }
                    }
                }
                if (anyNonEmptySources()) {
                    removeSources.setEnabled(true);
                    allowFormatParsing(true);
                } else {
                    removeSources.setEnabled(false);
                    allowFormatParsing(false);
                }
                sourcemodel.fireTableDataChanged();
                int r = fileselector.getRowCount() - 1;
                fileselector.getSelectionModel().setSelectionInterval(r, r);
            }
        });
        this.addSourcesFromStorage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                findFile.setVisible(true);
            }
        });
        this.addSourcesFromURL.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String yourURL = JOptionPane.showInputDialog("Please input a URL");
                if (yourURL != null) {
                    yourURL = yourURL.trim();
                }
                if ((yourURL != null) && (yourURL.length() > 0)) {
                    String currentRepos = repositoryView.getText();
                    FlatFileDOM dom = new FlatFileDOM();
                    try {
                        try {
                            URL url = new URL(yourURL);
                        } catch (java.net.MalformedURLException err1) {
                            yourURL = "http://" + yourURL;
                            URL url = new URL(yourURL);
                        }
                    } catch (Exception err) {
                        throw new RuntimeException("Invalid URL.", err);
                    }
                    dom.setNickname(getProposedNickname(currentRepos, yourURL, true));
                    dom.setFilename(yourURL);
                    dom.setEnabled("true");
                    dom.setIsURL("true");
                    sourcemodel.addFlatFile(dom);
                } else {
                    return;
                }
                if (anyNonEmptySources()) {
                    removeSources.setEnabled(true);
                    allowFormatParsing(true);
                } else {
                    removeSources.setEnabled(false);
                    allowFormatParsing(false);
                }
                sourcemodel.fireTableDataChanged();
                int r = fileselector.getRowCount() - 1;
                fileselector.getSelectionModel().setSelectionInterval(r, r);
            }
        });
        this.preview.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                complete_preview = false;
                waitIndicator.setText("|");
                Thread th = new Thread(new Runnable() {

                    public void run() {
                        while (true) {
                            String txt = waitIndicator.getText();
                            if (txt.equals("-")) {
                                waitIndicator.setText("\\");
                            }
                            if (txt.equals("\\")) {
                                waitIndicator.setText("|");
                            }
                            if (txt.equals("|")) {
                                waitIndicator.setText("/");
                            }
                            if (txt.equals("/")) {
                                waitIndicator.setText("-");
                            }
                            try {
                                Thread.sleep(100);
                            } catch (Exception err) {
                                throw new RuntimeException("Problem waiting in thread.", err);
                            }
                            waitIndicator.repaint();
                            if (complete_preview) {
                                waitIndicator.setText("X");
                                break;
                            }
                        }
                    }
                });
                th.start();
                Thread th2 = new Thread(new Runnable() {

                    public void run() {
                        FlatFileDOM[] filespecs = new FlatFileDOM[sourcemodel.getRowCount()];
                        for (int j = 0; j < sourcemodel.getRowCount(); j++) {
                            filespecs[j] = sourcemodel.getFlatFileDOM(j);
                        }
                        Vector hdrs = null;
                        Vector types = null;
                        for (int j = 0; j < filespecs.length; j++) {
                            DataSetReader rdr = new DataSetReader(filespecs[j]);
                            int rc = rdr.determineRowCount();
                            filespecs[j].setRowCount(rc);
                            if (j == 0) {
                                hdrs = rdr.getHeaders();
                                types = rdr.getTypes();
                            }
                        }
                        FlatFileSet dataset = new FlatFileSet(filespecs);
                        dataset.setSortProtocol(getSortProtocol());
                        for (int j = 0; j < hdrs.size(); j++) {
                            dataset.addColumn((String) hdrs.get(j), (Class) types.get(j));
                        }
                        System.out.println("The dataset rc is " + dataset.getRowCount());
                        previewPanel.setDataSet(dataset);
                        previewPanel.setVerticalScrollIntermittant(true);
                        previewPanel.setHorizontalScrollIntermittant(true);
                        previewPanel.setEditable(false);
                        if (anyNonEmptySources()) {
                            allowFormatParsing(true);
                        } else {
                            allowFormatParsing(false);
                        }
                        complete_preview = true;
                    }
                });
                th2.start();
            }
        });
        allowFormatParsing(false);
        this.formatTable.repaint();
        String last_repos = Prefs.getConfigValue("default", "lastrepository").trim();
        if (this.Config.hasRepository(last_repos)) {
            this.setRepository(last_repos);
        }
    }
