    public BuddiMenu(AbstractFrame frame) {
        super();
        this.frame = frame;
        final Application app = Application.getInstance();
        final JScreenMenu file = new JScreenMenu(Translate.getInstance().get(TranslateKeys.FILE));
        final JScreenMenu edit = new JScreenMenu(Translate.getInstance().get(TranslateKeys.EDIT));
        final JScreenMenu window = new JScreenMenu(Translate.getInstance().get(TranslateKeys.WINDOW));
        final JScreenMenu help = new JScreenMenu(Translate.getInstance().get(TranslateKeys.HELP));
        final JScreenMenu exports = new JScreenMenu(Translate.getInstance().get(TranslateKeys.EXPORT));
        final JScreenMenu imports = new JScreenMenu(Translate.getInstance().get(TranslateKeys.IMPORT));
        this.add(file);
        this.add(edit);
        this.add(window);
        this.add(help);
        final JScreenMenuItem open = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.OPEN_DATA_FILE));
        final JScreenMenuItem newFile = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.NEW_DATA_FILE));
        final JScreenMenuItem backup = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.BACKUP_DATA_FILE));
        final JScreenMenuItem restore = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.RESTORE_DATA_FILE));
        final JScreenMenuItem encrypt = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.ENCRYPT_DATA_FILE));
        final JScreenMenuItem decrypt = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.DECRYPT_DATA_FILE));
        final JScreenMenuItem print = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.PRINT));
        final JScreenMenuItem close = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.CLOSE_WINDOW));
        newFile.addUserFrame(MainBuddiFrame.class);
        open.addUserFrame(MainBuddiFrame.class);
        backup.addUserFrame(MainBuddiFrame.class);
        restore.addUserFrame(MainBuddiFrame.class);
        encrypt.addUserFrame(MainBuddiFrame.class);
        decrypt.addUserFrame(MainBuddiFrame.class);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        backup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        restore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        for (JScreenMenuItem menuItem : PluginFactory.getExportMenuItems(frame)) {
            exports.add(menuItem);
        }
        for (JScreenMenuItem menuItem : PluginFactory.getImportMenuItems(frame)) {
            imports.add(menuItem);
        }
        file.add(newFile);
        file.add(open);
        file.addSeparator();
        file.add(backup);
        file.add(restore);
        file.addSeparator();
        file.add(encrypt);
        file.add(decrypt);
        file.addSeparator();
        file.add(print);
        file.addSeparator();
        file.add(exports);
        file.add(imports);
        file.addSeparator();
        if (Buddi.isMac()) {
            file.add(close);
        }
        final JScreenMenuItem toggleReconciled = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.TOGGLE_RECONCILED));
        final JScreenMenuItem toggleCleared = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.TOGGLE_CLEARED));
        final JScreenMenuItem editAutomaticTransactions = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.EDIT_SCHEDULED_TRANSACTIONS));
        toggleReconciled.addUserFrame(TransactionsFrame.class);
        toggleCleared.addUserFrame(TransactionsFrame.class);
        editAutomaticTransactions.addUserFrame(MainBuddiFrame.class);
        editAutomaticTransactions.addUserFrame(TransactionsFrame.class);
        toggleReconciled.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        toggleCleared.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        if (PrefsInstance.getInstance().getPrefs().isShowAdvanced()) {
            edit.add(toggleCleared);
            edit.add(toggleReconciled);
            edit.addSeparator();
        }
        edit.add(editAutomaticTransactions);
        final JScreenMenuItem minimize = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.MINIMIZE));
        minimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        window.add(minimize);
        final JScreenMenuItem showHelp = new JScreenMenuItem(Translate.getInstance().get(TranslateKeys.BUDDI_HELP));
        showHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.SHIFT_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        help.add(showHelp);
        AboutJMenuItem about = app.getAboutJMenuItem();
        about.setText(Translate.getInstance().get(TranslateKeys.ABOUT_MENU_ITEM));
        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutDialog();
            }
        });
        if (!AboutJMenuItem.isAutomaticallyPresent()) help.add(about);
        help.addSeparator();
        help.add(DocumentationFactory.getDocumentsMenu());
        help.add(DocumentationFactory.getLicensesMenu());
        newFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                final JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setCurrentDirectory(new File(PrefsInstance.getInstance().getPrefs().getDataFile()));
                jfc.setDialogTitle(Translate.getInstance().get(TranslateKeys.CHOOSE_DATASTORE_LOCATION));
                if (jfc.showSaveDialog(MainBuddiFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.CANNOT_SAVE_OVER_DIR), Translate.getInstance().get(TranslateKeys.ERROR), JOptionPane.ERROR_MESSAGE); else {
                        if (jfc.getSelectedFile().exists()) {
                            if (JOptionPane.showConfirmDialog(null, Translate.getInstance().get(TranslateKeys.OVERWRITE_EXISTING_FILE_MESSAGE) + jfc.getSelectedFile(), Translate.getInstance().get(TranslateKeys.OVERWRITE_EXISTING_FILE), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                return;
                            }
                        }
                        final String location;
                        if (jfc.getSelectedFile().getName().endsWith(Const.DATA_FILE_EXTENSION)) location = jfc.getSelectedFile().getAbsolutePath(); else location = jfc.getSelectedFile().getAbsolutePath() + Const.DATA_FILE_EXTENSION;
                        DataInstance.getInstance().loadDataModel(new File(location), true);
                        PrefsInstance.getInstance().getPrefs().setDataFile(location);
                        MainBuddiFrame.getInstance().updateContent();
                        JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.NEW_DATA_FILE_SAVED) + location, Translate.getInstance().get(TranslateKeys.FILE_SAVED), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        open.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                final JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setCurrentDirectory(new File(PrefsInstance.getInstance().getPrefs().getDataFile()));
                jfc.setFileHidingEnabled(true);
                jfc.setFileFilter(new FileFilter() {

                    public boolean accept(File arg0) {
                        if (arg0.isDirectory() || arg0.getName().endsWith(Const.DATA_FILE_EXTENSION)) return true; else return false;
                    }

                    public String getDescription() {
                        return Translate.getInstance().get(TranslateKeys.BUDDI_FILE_DESC);
                    }
                });
                jfc.setDialogTitle(Translate.getInstance().get(TranslateKeys.OPEN_DATA_FILE_TITLE));
                if (jfc.showOpenDialog(MainBuddiFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) {
                        if (Const.DEVEL) Log.debug(Translate.getInstance().get(TranslateKeys.MUST_SELECT_BUDDI_FILE));
                    } else {
                        if (JOptionPane.showConfirmDialog(null, Translate.getInstance().get(TranslateKeys.CONFIRM_LOAD_BACKUP_FILE), Translate.getInstance().get(TranslateKeys.CLOSE_DATA_FILE), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            if (Const.DEVEL) Log.debug("Loading file " + jfc.getSelectedFile().getAbsolutePath());
                            DataInstance.getInstance().loadDataModel(jfc.getSelectedFile(), false);
                            PrefsInstance.getInstance().getPrefs().setDataFile(jfc.getSelectedFile().getAbsolutePath());
                            PrefsInstance.getInstance().savePrefs();
                            MainBuddiFrame.getInstance().updateContent();
                            JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.SUCCESSFUL_OPEN_FILE) + jfc.getSelectedFile().getAbsolutePath().toString(), Translate.getInstance().get(TranslateKeys.OPENED_FILE), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.CANCELLED_FILE_LOAD_MESSAGE), Translate.getInstance().get(TranslateKeys.CANCELLED_FILE_LOAD), JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                MainBuddiFrame.getInstance().updateContent();
            }
        });
        backup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                final JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle(Translate.getInstance().get(TranslateKeys.CHOOSE_BACKUP_FILE));
                if (jfc.showSaveDialog(MainBuddiFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) if (Const.DEVEL) Log.debug(Translate.getInstance().get(TranslateKeys.CANNOT_SAVE_OVER_DIR)); else {
                        final String location;
                        if (jfc.getSelectedFile().getName().endsWith(Const.DATA_FILE_EXTENSION)) location = jfc.getSelectedFile().getAbsolutePath(); else location = jfc.getSelectedFile().getAbsolutePath() + Const.DATA_FILE_EXTENSION;
                        DataInstance.getInstance().saveDataModel(location);
                        JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.SUCCESSFUL_BACKUP) + location, Translate.getInstance().get(TranslateKeys.FILE_SAVED), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        restore.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                final JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setFileHidingEnabled(true);
                jfc.setFileFilter(new FileFilter() {

                    public boolean accept(File arg0) {
                        if (arg0.isDirectory() || arg0.getName().endsWith(Const.DATA_FILE_EXTENSION)) return true; else return false;
                    }

                    public String getDescription() {
                        return Translate.getInstance().get(TranslateKeys.BUDDI_FILE_DESC);
                    }
                });
                jfc.setDialogTitle(Translate.getInstance().get(TranslateKeys.RESTORE_DATA_FILE));
                if (jfc.showOpenDialog(MainBuddiFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().isDirectory()) {
                        if (Const.DEVEL) Log.debug(Translate.getInstance().get(TranslateKeys.MUST_SELECT_BUDDI_FILE));
                    } else {
                        if (JOptionPane.showConfirmDialog(null, Translate.getInstance().get(TranslateKeys.CONFIRM_RESTORE_BACKUP_FILE), Translate.getInstance().get(TranslateKeys.RESTORE_DATA_FILE), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            File oldFile = new File(PrefsInstance.getInstance().getPrefs().getDataFile());
                            try {
                                FileFunctions.copyFile(jfc.getSelectedFile(), oldFile);
                                DataInstance.getInstance().loadDataModel(oldFile, false);
                                MainBuddiFrame.getInstance().updateContent();
                                JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.SUCCESSFUL_RESTORE_FILE) + jfc.getSelectedFile().getAbsolutePath().toString(), Translate.getInstance().get(TranslateKeys.RESTORED_FILE), JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ioe) {
                                JOptionPane.showMessageDialog(null, ioe, "Flagrant System Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.CANCEL_FILE_RESTORE_MESSAGE), Translate.getInstance().get(TranslateKeys.CANCELLED_FILE_RESTORE), JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
        encrypt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (JOptionPane.showConfirmDialog(MainBuddiFrame.getInstance(), Translate.getInstance().get(TranslateKeys.ENCRYPT_DATA_FILE_WARNING), Translate.getInstance().get(TranslateKeys.CHANGE_ENCRYPTION), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                    DataInstance.getInstance().createNewCipher(true);
                    DataInstance.getInstance().saveDataModel();
                }
            }
        });
        decrypt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (JOptionPane.showConfirmDialog(MainBuddiFrame.getInstance(), Translate.getInstance().get(TranslateKeys.DECRYPT_DATA_FILE_WARNING), Translate.getInstance().get(TranslateKeys.CHANGE_ENCRYPTION), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                    DataInstance.getInstance().createNewCipher(false);
                    DataInstance.getInstance().saveDataModel();
                }
            }
        });
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (BuddiMenu.this.frame instanceof AbstractFrame) {
                    Component toPrint = ((AbstractFrame) BuddiMenu.this.frame).getPrintedComponent();
                    if (toPrint != null) {
                        PrintUtilities pu = new PrintUtilities(toPrint);
                        pu.print();
                    } else JOptionPane.showMessageDialog(null, Translate.getInstance().get(TranslateKeys.NOTHING_TO_PRINT), Translate.getInstance().get(TranslateKeys.PRINT_ERROR), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        toggleCleared.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ((TransactionsFrame) BuddiMenu.this.frame).toggleCleared();
            }
        });
        toggleReconciled.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ((TransactionsFrame) BuddiMenu.this.frame).toggleReconciled();
            }
        });
        editAutomaticTransactions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new ScheduledTransactionsListFrame().openWindow();
            }
        });
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (BuddiMenu.this.frame != null) BuddiMenu.this.frame.setVisible(false);
            }
        });
        minimize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (BuddiMenu.this.frame != null) BuddiMenu.this.frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
        showHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                try {
                    final String location = Const.PROJECT_URL + PrefsInstance.getInstance().getPrefs().getLanguage().replaceAll("-.*$", "") + "/index.php";
                    if (Const.DEVEL) Log.debug("Trying to open Help at " + location + "...");
                    BrowserLauncher bl = new BrowserLauncher(null);
                    bl.openURLinBrowser(location);
                } catch (Exception e) {
                    Log.error(e);
                }
            }
        });
        PreferencesJMenuItem preferences = app.getPreferencesJMenuItem();
        preferences.setText(Translate.getInstance().get(TranslateKeys.PREFERENCES_MENU_ITEM));
        preferences.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PreferencesDialog().clearContent().openWindow();
            }
        });
        if (!PreferencesJMenuItem.isAutomaticallyPresent()) edit.add(preferences);
        QuitJMenuItem quit = app.getQuitJMenuItem();
        quit.setText(Translate.getInstance().get(TranslateKeys.QUIT_MENU_ITEM));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MainBuddiFrame.getInstance() != null) MainBuddiFrame.getInstance().savePosition();
                if (Const.DEVEL) Log.debug("Exiting");
                System.exit(0);
            }
        });
        if (!QuitJMenuItem.isAutomaticallyPresent()) file.add(quit);
    }
