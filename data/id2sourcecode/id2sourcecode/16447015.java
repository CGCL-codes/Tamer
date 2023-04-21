    private void initComponents() {
        setSize(DIALOG_SIZE);
        setTitle(UIHelper.getText("editres.addpackage"));
        setIconImage(AdminUIUtils.getAdminWindowIcon());
        getContentPane().setLayout(new BorderLayout(0, 0));
        {
            JPanel buttonPane = new JPanel();
            FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
            buttonPane.setLayout(fl_buttonPane);
            getContentPane().add(buttonPane, BorderLayout.PAGE_END);
            {
                final JButton saveButton = new JButton();
                saveButton.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        List<ResourceDataVO> resources = tablePanel.getData();
                        ComboBoxItem appl = (ComboBoxItem) applicationComboBox.getSelectedItem();
                        boolean allOk = true;
                        for (ResourceDataVO resource : resources) {
                            resource.setApplication(appl.getValue());
                            resource.setOrgId(orgId);
                            allOk &= checkValues(resource);
                        }
                        if (allOk) {
                            if (resources.size() != 0) {
                                UploadResourceDialog res = new UploadResourceDialog(resources, thisDialog);
                                res.setLocationRelativeTo(thisDialog);
                                res.setVisible(true);
                            }
                            dispose();
                        }
                    }
                });
                saveButton.setText(UIHelper.getText("editres.add"));
                saveButton.setIcon(UIHelper.getImage("enable.png"));
                buttonPane.add(saveButton);
                getRootPane().setDefaultButton(saveButton);
            }
            {
                JButton cancelButton = new JButton();
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setText(UIHelper.getText("cancel"));
                cancelButton.setIcon(UIHelper.getImage("disable.png"));
                buttonPane.add(cancelButton);
            }
        }
        {
            JSplitPane mainSplitPane = new JSplitPane();
            mainSplitPane.setResizeWeight(0.0);
            mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            mainSplitPane.setDividerSize(0);
            getContentPane().add(mainSplitPane, BorderLayout.CENTER);
            {
                tablePanel = new ImportResourcesTablePanel(new ArrayList<ResourceDataVO>());
                scrollPane = new JScrollPane(tablePanel);
                mainSplitPane.setRightComponent(scrollPane);
                JPanel topPanel = new JPanel(new GridBagLayout());
                mainSplitPane.setLeftComponent(topPanel);
                {
                    GridBagConstraints constraints;
                    constraints = new GridBagConstraints();
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.insets = new Insets(10, 10, 10, 10);
                    JLabel logoLabel = new JLabel();
                    logoLabel.setIcon(UIHelper.getImage("add_resource_package.gif"));
                    topPanel.add(logoLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    constraints.insets = new Insets(10, 10, 10, 10);
                    JLabel titleLabel = new JLabel();
                    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    titleLabel.setText(UIHelper.getText("editres.addpackage"));
                    titleLabel.setFont(UIHelper.getTitleFont());
                    topPanel.add(titleLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.insets = new Insets(10, 40, 10, 10);
                    constraints.anchor = GridBagConstraints.EAST;
                    JLabel keyLabel = new JLabel();
                    keyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                    keyLabel.setText(UIHelper.getText("editres.file") + ":");
                    keyLabel.setFont(UIHelper.getLabelFontBold());
                    topPanel.add(keyLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 1;
                    constraints.insets = new Insets(10, 10, 10, 10);
                    selectFileButton = new JButton();
                    selectFileButton.addActionListener(new ActionListener() {

                        public void actionPerformed(final ActionEvent e) {
                            JFileChooser fileChooser = new JFileChooser(CommonUtils.getCurrentSaveDirectory());
                            fileChooser.setFileFilter(new FileFilter() {

                                @Override
                                public boolean accept(File f) {
                                    if (f.isDirectory()) {
                                        return true;
                                    }
                                    if (f.canRead() && f.isFile() && f.getName().toLowerCase().endsWith(".zip")) {
                                        return true;
                                    }
                                    return false;
                                }

                                @Override
                                public String getDescription() {
                                    return UIHelper.getText("editres.packagedescription");
                                }
                            });
                            int returnVal = fileChooser.showOpenDialog(thisDialog);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                CommonUtils.setCurrentSaveDirectory(fileChooser.getSelectedFile());
                                ArrayList<ResourceDataVO> resources = new ArrayList<ResourceDataVO>();
                                File zipFile = fileChooser.getSelectedFile();
                                Properties props = new Properties();
                                ZipInputStream packageStream = null;
                                try {
                                    packageStream = new ZipInputStream(new FileInputStream(zipFile));
                                    ZipEntry entry = null;
                                    while ((entry = packageStream.getNextEntry()) != null) {
                                        if (!entry.isDirectory()) {
                                            if (entry.getName().equals(Constants.CUSTOMRESOURCE_PACKAGEINFOFILENAME)) {
                                                props.load(packageStream);
                                            } else {
                                                ResourceDataVO resourceData = new ResourceDataVO();
                                                resourceData.setName(entry.getName());
                                                byte[] buffer = new byte[8192];
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                int read = -1;
                                                while ((read = packageStream.read(buffer)) != -1) {
                                                    baos.write(buffer, 0, read);
                                                }
                                                resourceData.setData(baos.toByteArray());
                                                resources.add(resourceData);
                                            }
                                        }
                                    }
                                    fileErrorLabel.setText("");
                                    ShowPackageHeaderDialog hDialog = new ShowPackageHeaderDialog(props, thisDialog);
                                    hDialog.setLocationRelativeTo(SwingUtilities.getRootPane(thisDialog));
                                    hDialog.setModal(true);
                                    hDialog.setVisible(true);
                                    if (hDialog.isConfirmed()) {
                                        String version = hDialog.getVersion();
                                        String defaultComment = hDialog.getDefaultComment();
                                        String application = hDialog.getApplication();
                                        for (ResourceDataVO next : resources) {
                                            next.setApplication(application);
                                            next.setComment(defaultComment);
                                            next.setVersion(version);
                                            next.setOrgId(orgId);
                                            long currentDate = System.currentTimeMillis();
                                            next.setCreated(currentDate);
                                            next.setModified(currentDate);
                                            next.setType(AdminUIUtils.autoSenseCustomFileType(next.getName()));
                                        }
                                        tablePanel.setData(resources);
                                        for (int i = 0; i < applicationComboBox.getItemCount(); i++) {
                                            if (((ComboBoxItem) applicationComboBox.getItemAt(i)).getValue().equals(application)) {
                                                applicationComboBox.setSelectedIndex(i);
                                                break;
                                            }
                                        }
                                        versionValueLabel.setText(version);
                                    }
                                } catch (FileNotFoundException e1) {
                                    LocalLog.getLogger().log(Level.SEVERE, "Error reading resource package file : " + zipFile.getAbsoluteFile(), e);
                                    fileErrorLabel.setText(UIHelper.getText("fileerr.notexist"));
                                } catch (IOException e1) {
                                    LocalLog.getLogger().log(Level.SEVERE, "Error reading resource package file : " + zipFile.getAbsoluteFile(), e);
                                    fileErrorLabel.setText(UIHelper.getText("fileerr.errorreading"));
                                } finally {
                                    if (packageStream != null) {
                                        try {
                                            packageStream.close();
                                        } catch (IOException e1) {
                                            LocalLog.getLogger().log(Level.SEVERE, "Error closing resource package file : " + zipFile.getAbsoluteFile(), e);
                                        }
                                    }
                                }
                            }
                        }
                    });
                    selectFileButton.setIcon(UIHelper.getImage("importfile.png"));
                    selectFileButton.setText(UIHelper.getText("editglobprops.load") + " ...");
                    topPanel.add(selectFileButton, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 2;
                    constraints.insets = new Insets(0, 10, 0, 10);
                    fileErrorLabel = new JLabel("");
                    fileErrorLabel.setForeground(UIHelper.getErrorMsgForeground());
                    topPanel.add(fileErrorLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    constraints.insets = new Insets(0, 10, 0, 10);
                    constraints.anchor = GridBagConstraints.EAST;
                    JLabel orgTextLabel = new JLabel();
                    orgTextLabel.setText(UIHelper.getText("organization") + ":");
                    orgTextLabel.setFont(UIHelper.getLabelFontBold());
                    topPanel.add(orgTextLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 3;
                    constraints.insets = new Insets(0, 10, 0, 10);
                    JLabel orgLabel = new JLabel();
                    orgLabel.setText(orgName);
                    topPanel.add(orgLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 4;
                    constraints.insets = new Insets(5, 10, 0, 10);
                    constraints.anchor = GridBagConstraints.EAST;
                    JLabel applicationLabel = new JLabel();
                    applicationLabel.setText(UIHelper.getText("editglobprops.application") + ":");
                    applicationLabel.setFont(UIHelper.getLabelFontBold());
                    topPanel.add(applicationLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 4;
                    constraints.insets = new Insets(5, 10, 0, 10);
                    applicationComboBox = AdminUIUtils.genApplicationSelectComboBox(null);
                    topPanel.add(applicationComboBox, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 0.0;
                    constraints.gridwidth = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 5;
                    constraints.insets = new Insets(5, 10, 0, 10);
                    constraints.anchor = GridBagConstraints.EAST;
                    final JLabel versionLabel = new JLabel();
                    versionLabel.setFont(UIHelper.getLabelFontBold());
                    versionLabel.setText(UIHelper.getText("editres.version"));
                    topPanel.add(versionLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 1;
                    constraints.gridy = 5;
                    constraints.insets = new Insets(5, 10, 0, 10);
                    versionValueLabel = new JLabel();
                    versionValueLabel.setText("");
                    topPanel.add(versionValueLabel, constraints);
                    constraints = new GridBagConstraints();
                    constraints.weightx = 1.0;
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                    constraints.anchor = GridBagConstraints.NORTHWEST;
                    constraints.gridx = 0;
                    constraints.gridy = 6;
                    constraints.insets = new Insets(10, 10, 10, 10);
                    errorLabel = new JLabel();
                    errorLabel.setForeground(UIHelper.getErrorMsgForeground());
                    topPanel.add(errorLabel, constraints);
                }
            }
        }
    }
