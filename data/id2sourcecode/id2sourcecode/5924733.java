    private void initComponents() {
        setSize(DIALOG_SIZE);
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("34dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("103dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("17dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow(1.0)"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("96dlu"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("33dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("43dlu:grow(1.0)"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC }));
        setTitle(UIHelper.getText("mantokens.batchimport"));
        setIconImage(AdminUIUtils.getAdminWindowIcon());
        tosp = new TokenOutputSelectPanel(getOrgGS(), true);
        getContentPane().add(tosp, new CellConstraints(2, 12, 9, 1));
        kstsp = new KeyStoreTypeSelectPanel(tosp);
        getContentPane().add(kstsp, new CellConstraints(2, 10, 9, 1));
        titleLabel = new JLabel();
        titleLabel.setText(UIHelper.getText("mantokens.batchimport"));
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setIcon(UIHelper.getImage("batch_add_certs.png"));
        getContentPane().add(titleLabel, new CellConstraints(4, 2, 7, 1));
        resultLabel = new JLabel();
        resultLabel.setText(UIHelper.getText("mantokens.result") + ":");
        resultLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(resultLabel, new CellConstraints(4, 4, CellConstraints.LEFT, CellConstraints.DEFAULT));
        fileNameLabel = new JLabel();
        fileNameLabel.setText(UIHelper.getText("mantokens.filename") + ":");
        getContentPane().add(fileNameLabel, new CellConstraints(4, 4, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        getContentPane().add(getFileNameTextField(), new CellConstraints(6, 4, 3, 1));
        browseButton = new JButton();
        browseButton.setText(UIHelper.getText("browse"));
        browseButton.setIcon(UIHelper.getImage("fileopen.png"));
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(CommonUtils.getCurrentSaveDirectory());
                int returnVal = fileChooser.showOpenDialog(thisPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    CommonUtils.setCurrentSaveDirectory(fileChooser.getSelectedFile());
                    getFileNameTextField().setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        getContentPane().add(browseButton, new CellConstraints(10, 4));
        outputTextPane = new JTextPane();
        outputTextPane.setEditable(false);
        sp = new JScrollPane(outputTextPane);
        sp.setVisible(false);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(sp, new CellConstraints(4, 6, 7, 11, CellConstraints.FILL, CellConstraints.FILL));
        importButton = new JButton();
        importButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                clearOutput();
                if (!confirmImport()) {
                    return;
                }
                showResultComponents(true);
                try {
                    String filename = getFileNameTextField().getText().trim();
                    validateFileName(filename);
                    final File file = new File(filename);
                    final boolean sendNotifications = tosp.sendNotification();
                    final String outputDir = tosp.getOutputDir();
                    validateOutputDir(sendNotifications, outputDir);
                    final String keyStoreType = kstsp.getKeyStoreType();
                    validateKeyStoreType(sendNotifications, keyStoreType);
                    final String columnSeparator = columnSeparatorTextField.getText();
                    validateColumnSeparator(columnSeparator);
                    final String tokenType = nttsp.getSelectedToken();
                    InterfaceFactory.getAdministratorSettings().setProperty("importusers.column", columnSeparator);
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                inProcess = true;
                                IBatchUserImportFileParser parser = AdminInterfacesFactory.getBatchUserImportFileAnalyser(orgVO).getParser(tokenType, keyStoreType, orgVO, columnSeparator, file);
                                if (parser == null) {
                                    addErrorRow(UIHelper.getText("mantokens.unsupportedimportfile"));
                                    inProcess = false;
                                    return;
                                }
                                String tokenTypeName = getOrgGS().getDisplayTokenSelector().getName(tokenType);
                                ITokenAdminManager tam = AdminInterfacesFactory.getTokenAdminManager();
                                int success = 0;
                                int failures = 0;
                                setProcess(true);
                                while (getProcess() && parser.hasMore()) {
                                    boolean userImported = false;
                                    try {
                                        try {
                                            UserDataVO user = parser.next();
                                            UserDataVO currentUser = tam.fetchUser(user.getOrgId(), user.getUserDataVOWS().getUsername(), false);
                                            tam.editUser(user);
                                            RecentUpdatedUsersUpdater.getInstance().addUser(user);
                                            if (currentUser == null) {
                                                infoRow(UIHelper.getText("mantokens.userwithid") + ": " + user.getUniqueId() + " " + UIHelper.getText("mantokens.addsuccess"));
                                            } else {
                                                infoRow(UIHelper.getText("mantokens.userwithid") + ": " + user.getUniqueId() + " " + UIHelper.getText("mantokens.updatedsuccess"));
                                            }
                                            if (sendNotifications) {
                                                NotificationMessageTemplate msgTmpl = tosp.getNotificationMessageTemplatet();
                                                String subject = orgGS.getMessagePopulator().populate(msgTmpl.getSubject(), user, tokenTypeName, null);
                                                String message = orgGS.getMessagePopulator().populate(msgTmpl.getMessage(), user, tokenTypeName, null);
                                                try {
                                                    tam.sendNotification(orgVO, user.getUsername(), user.getEmail(), msgTmpl.getCarbonCopyOf(), subject, message);
                                                    userImported = true;
                                                    infoRow(UIHelper.getText("mantokens.notificationsent") + ": " + user.getUniqueId());
                                                } catch (IOException ioe) {
                                                    errorRow(UIHelper.getText("mantokens.errorsendingnotification") + ": " + user.getUniqueId() + ", " + UIHelper.getText("mantokens.errormessage") + ": " + ioe.getMessage());
                                                }
                                            } else {
                                                String userIdDisplayMessage = user.getUniqueId();
                                                if (userIdDisplayMessage == null) {
                                                    userIdDisplayMessage = UIHelper.getText("mantokens.noid");
                                                }
                                                try {
                                                    byte[] data = tam.getKeyStoreData(user.getUserDataVOWS().getUsername(), user.getUserDataVOWS().getPassword(), kstsp.getKeyAlg(), kstsp.getKeySpec());
                                                    String fileEnding = AdminUIUtils.getKeyStoreFileEnding(keyStoreType);
                                                    String fileName = user.getUserDataVOWS().getUsername() + "." + fileEnding;
                                                    FileOutputStream fos = new FileOutputStream(outputDir + "/" + fileName);
                                                    fos.write(data);
                                                    fos.close();
                                                    AdminUIUtils.writePasswordFile(outputDir + "/" + user.getUserDataVOWS().getUsername() + "_pwd.txt", user);
                                                    tam.fetchUser(user.getOrgId(), user.getUserDataVOWS().getUsername(), true);
                                                    userImported = true;
                                                    infoRow(UIHelper.getText("mantokens.keystoregenerated") + ": " + userIdDisplayMessage + ", " + UIHelper.getText("mantokens.filenamesmall") + ": " + fileName);
                                                } catch (IOException ioe) {
                                                    errorRow(UIHelper.getText("mantokens.keystoregenerationfailed") + ": " + userIdDisplayMessage + ", " + UIHelper.getText("mantokens.errormessage") + ": " + ioe.getMessage());
                                                }
                                            }
                                        } catch (TokenDataInvalidException e1) {
                                            if (e1.getUniqueId() == null) {
                                                errorRow(UIHelper.getText("mantokens.invaliduserdatanouser") + ", " + UIHelper.getText("mantokens.errormessage") + ": " + e1.getMessage());
                                            } else {
                                                errorRow(UIHelper.getText("mantokens.invaliduserdata") + ": " + e1.getUniqueId() + ", " + UIHelper.getText("mantokens.errormessage") + ": " + e1.getMessage());
                                            }
                                            errorRow("\n");
                                            LocalLog.getLogger().log(Level.SEVERE, "Error parsing user data, user id " + e1.getUniqueId() + ", message: " + e1.getMessage(), e);
                                        } catch (IOException e1) {
                                            errorRow(UIHelper.getText("mantokens.ioerr"));
                                            LocalLog.getLogger().log(Level.SEVERE, "Error importing user data.", e1);
                                        }
                                        if (userImported) {
                                            success++;
                                        } else {
                                            failures++;
                                        }
                                    } catch (AuthorizationDeniedException_Exception e1) {
                                        errorRow(UIHelper.getText("mantokens.notauthorizedtoimport"));
                                        LocalLog.getLogger().log(Level.SEVERE, "Error importing user data.", e1);
                                    }
                                }
                                infoRow("\n" + UIHelper.getText("mantokens.numsuccessimports") + ": " + success);
                                if (failures == 0) {
                                    infoRow(UIHelper.getText("mantokens.nofailedimports"));
                                } else {
                                    errorRow(UIHelper.getText("mantokens.numfailedimports") + ": " + failures);
                                }
                            } finally {
                                inProcess = false;
                            }
                        }

                        private void infoRow(final String infoMsg) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    addInfoRow(infoMsg);
                                }
                            });
                        }

                        private void errorRow(final String errorMsg) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    addErrorRow(errorMsg);
                                }
                            });
                        }
                    });
                    t.start();
                } catch (IllegalArgumentException e1) {
                    addErrorRow(e1.getMessage());
                    inProcess = false;
                }
            }

            private void validateFileName(String filename) throws IllegalArgumentException {
                String result = AdminUIUtils.isFileReadable(filename);
                if (result != null) {
                    throw new IllegalArgumentException(result);
                }
            }

            private void validateOutputDir(boolean sendNotifications, String outDir) throws IllegalArgumentException {
                if (!sendNotifications) {
                    String result = AdminUIUtils.isDirectoryWritable(outDir);
                    if (result != null) {
                        throw new IllegalArgumentException(result);
                    }
                }
            }

            private void validateColumnSeparator(String colSep) throws IllegalArgumentException {
                if (colSep == null || colSep.equals("")) {
                    throw new IllegalArgumentException(UIHelper.getText("mantokens.nocolsep"));
                }
            }

            private void validateKeyStoreType(boolean useNotification, String keyStoreType) throws IllegalArgumentException {
                if (!useNotification && keyStoreType.equals("USERGENERATED")) {
                    throw new IllegalArgumentException(UIHelper.getText("mantokens.cannotgenusergenerated"));
                }
            }
        });
        importButton.setText(UIHelper.getText("mantokens.importusers"));
        importButton.setIcon(UIHelper.getImage("batch_add_certs.png"));
        getContentPane().add(importButton, new CellConstraints(10, 18));
        closeButton1 = new JButton();
        closeButton1.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        closeButton1.setIcon(UIHelper.getImage("exit.png"));
        closeButton1.setText(UIHelper.getText("close"));
        getContentPane().add(closeButton1, new CellConstraints(4, 18, CellConstraints.DEFAULT, CellConstraints.FILL));
        closeButton2 = new JButton();
        closeButton2.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (inProcess) {
                    if (confirmAbort()) {
                        setProcess(false);
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
        closeButton2.setIcon(UIHelper.getImage("exit.png"));
        closeButton2.setText(UIHelper.getText("close"));
        closeButton2.setVisible(false);
        getContentPane().add(closeButton2, new CellConstraints(10, 18, CellConstraints.DEFAULT, CellConstraints.FILL));
        backButton = new JButton();
        backButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (inProcess) {
                    if (confirmAbort()) {
                        setProcess(false);
                        showResultComponents(false);
                    }
                } else {
                    showResultComponents(false);
                }
            }
        });
        backButton.setIcon(UIHelper.getImage("back.gif"));
        backButton.setText(UIHelper.getText("back"));
        backButton.setVisible(false);
        getContentPane().add(backButton, new CellConstraints(4, 18, CellConstraints.DEFAULT, CellConstraints.FILL));
        columnSeparatorLabel = new JLabel();
        columnSeparatorLabel.setText(UIHelper.getText("mantokens.columnsep") + ":");
        getContentPane().add(columnSeparatorLabel, new CellConstraints(2, 6, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        columnSeparatorTextField = new JTextField();
        columnSeparatorTextField.setText(InterfaceFactory.getAdministratorSettings().getProperty("importusers.column", ","));
        getContentPane().add(columnSeparatorTextField, new CellConstraints(6, 6, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel columnSeparatorHelpLabel = new JLabel();
        AdminUIUtils.setNoDelayToolTip(columnSeparatorHelpLabel);
        columnSeparatorHelpLabel.setIcon(UIHelper.getImage("help.png"));
        columnSeparatorHelpLabel.setToolTipText(AdminUIUtils.formatMultilineToolTipIntoHTML("mantokens.columnsephelp"));
        getContentPane().add(columnSeparatorHelpLabel, new CellConstraints(8, 6));
        nttsp = new NewTokenTypeSelectPanel(orgGS, tokenDataGeneratorFactory, "mantokens.batchtokenhelp");
        getContentPane().add(nttsp, new CellConstraints(2, 8, 9, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        showResultComponents(false);
    }
