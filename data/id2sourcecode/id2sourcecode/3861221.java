    @Override
    public Control createContents(final Composite parent) {
        fIsInitializing = true;
        fContainer = new Composite(parent, SWT.NULL);
        fContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fContainer.setLayout(new GridLayout(1, false));
        Composite inputDirectoriesContainer = new Composite(fContainer, SWT.NULL);
        inputDirectoriesContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        inputDirectoriesContainer.setLayout(new GridLayout(2, false));
        Label lblInputDirectories = new Label(inputDirectoriesContainer, SWT.NONE);
        lblInputDirectories.setText("Enter all directories to include in scan:");
        lblInputDirectories.pack();
        lblInputDirectories.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtInputDirectoriesInclude = new Text(inputDirectoriesContainer, SWT.BORDER);
        fTxtInputDirectoriesInclude.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Button btnAddDirectory = new Button(inputDirectoriesContainer, SWT.PUSH);
        Image imgAdd = new Image(PlatformUI.getWorkbench().getDisplay(), getClass().getResourceAsStream("/icons/add.gif"));
        btnAddDirectory.setImage(imgAdd);
        btnAddDirectory.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.OPEN | SWT.MULTI);
                dialog.setText("Add Directory");
                String path = dialog.open();
                if (path != null && !path.equals("")) {
                    File file = new File(path);
                    if (file != null && file.isDirectory()) {
                        fTxtInputDirectoriesInclude.setText(fTxtInputDirectoriesInclude.getText() + path + ";");
                    }
                }
            }
        });
        Composite inputDirectoriesExcludeContainer = new Composite(fContainer, SWT.NULL);
        inputDirectoriesExcludeContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        inputDirectoriesExcludeContainer.setLayout(new GridLayout(2, false));
        Label lblInputDirectoriesExclude = new Label(inputDirectoriesExcludeContainer, SWT.NONE);
        lblInputDirectoriesExclude.setText("Enter all directories to exclude in scan:");
        lblInputDirectoriesExclude.pack();
        lblInputDirectoriesExclude.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtInputDirectoriesExclude = new Text(inputDirectoriesExcludeContainer, SWT.BORDER);
        fTxtInputDirectoriesExclude.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Button btnAddDirectoryExclude = new Button(inputDirectoriesExcludeContainer, SWT.PUSH);
        btnAddDirectoryExclude.setImage(imgAdd);
        btnAddDirectoryExclude.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.OPEN | SWT.MULTI);
                dialog.setText("Add Directory");
                String path = dialog.open();
                if (path != null && !path.equals("")) {
                    File file = new File(path);
                    if (file != null && file.isDirectory()) {
                        fTxtInputDirectoriesExclude.setText(fTxtInputDirectoriesExclude.getText() + path + ";");
                    }
                }
            }
        });
        Composite fileExtensionsExcludeContainer = new Composite(fContainer, SWT.NULL);
        fileExtensionsExcludeContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fileExtensionsExcludeContainer.setLayout(new GridLayout(2, false));
        Label lblFileExtensionsExclude = new Label(fileExtensionsExcludeContainer, SWT.NONE);
        lblFileExtensionsExclude.setText("Enter all file extensions to exclude in scan:");
        lblFileExtensionsExclude.pack();
        lblFileExtensionsExclude.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtFileExtensionsExclude = new Text(fileExtensionsExcludeContainer, SWT.BORDER);
        fTxtFileExtensionsExclude.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        Composite lookBackContainer = new Composite(fContainer, SWT.NULL);
        lookBackContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        lookBackContainer.setLayout(new GridLayout(2, false));
        Label lblLookback = new Label(lookBackContainer, SWT.NONE);
        lblLookback.setText("Specify how far back to include directories:");
        lblLookback.pack();
        lblLookback.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtLookbackValue = new Text(lookBackContainer, SWT.BORDER);
        fTxtLookbackValue.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!fIsInitializing) {
                    try {
                        Long.parseLong(fTxtLookbackValue.getText().trim());
                    } catch (NumberFormatException e) {
                        fTxtLookbackValue.setText("");
                    }
                }
            }
        });
        fCboLookbackUnits = new Combo(lookBackContainer, SWT.READ_ONLY);
        fCboLookbackUnits.add("day(s)");
        fCboLookbackUnits.add("hour(s)");
        Group grpAutomaticScanning = new Group(fContainer, SWT.NULL);
        grpAutomaticScanning.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        grpAutomaticScanning.setLayout(new GridLayout(2, false));
        grpAutomaticScanning.setText("Automatic Scanning");
        fBtnAutomaticScanning = new Button(grpAutomaticScanning, SWT.CHECK);
        fBtnAutomaticScanning.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fBtnAutomaticScanning.setText("Enable automatic scanning of input directories");
        fBtnAutomaticScanning.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (fBtnAutomaticScanning.getSelection() && SystemTray.isSupported() && !PrefManager.showApplicationInSystemTray()) {
                    if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Enable Close To System Tray?", "This preference is made even more powerful when combined with the 'Close to the system tray' preference.  " + "With both preference enabled Ulmac will be able to scan your input directories for new files without taking up any screen real estate.\n\n" + "Would you like to turn the 'Close to the system tray' preference on?")) {
                        getPreferenceStore().putValue(IPreferenceIds.GENERAL_SHOW_IN_SYSTEM_TRAY, "true");
                    }
                }
            }
        });
        Label lblAutomaticScanning = new Label(grpAutomaticScanning, SWT.NONE);
        lblAutomaticScanning.setText("Specify how often the automatic scan should run:");
        lblAutomaticScanning.pack();
        lblAutomaticScanning.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtAutomaticScanningValue = new Text(grpAutomaticScanning, SWT.BORDER);
        fTxtAutomaticScanningValue.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!fIsInitializing) {
                    try {
                        Long.parseLong(fTxtAutomaticScanningValue.getText().trim());
                    } catch (NumberFormatException e) {
                        fTxtAutomaticScanningValue.setText("");
                    }
                }
            }
        });
        fCboAutomaticScanningUnits = new Combo(grpAutomaticScanning, SWT.READ_ONLY);
        fCboAutomaticScanningUnits.add("hour(s)");
        fCboAutomaticScanningUnits.add("minute(s)");
        Composite bottomContainer = new Composite(fContainer, SWT.NULL);
        bottomContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        bottomContainer.setLayout(new GridLayout(1, false));
        fBtnPromptForValue = new Button(bottomContainer, SWT.CHECK);
        fBtnPromptForValue.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fBtnPromptForValue.setText("Prompt for look back value");
        fBtnScanOnStartup = new Button(bottomContainer, SWT.CHECK);
        fBtnScanOnStartup.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fBtnScanOnStartup.setText("Perform scan of input directories on startup");
        fBtnCopyMP3s = new Button(bottomContainer, SWT.CHECK);
        fBtnCopyMP3s.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fBtnCopyMP3s.setText("Disable tagging on MP3 files (files will be copied to the default output directory)");
        fBtnCopyMP3s.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (fBtnCopyMP3s.getSelection() && (fTxtFileExtensionsExclude.getText().contains("mp3") || fTxtFileExtensionsExclude.getText().contains("MP3"))) {
                    if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Remove MP3 from Exclude File Option?", "In order for MP3s to be copied with this preference the exclude file options will need to be modified.\n\n" + "Would you like to remove MP3 from this option?")) {
                        String oldFileExtensionsExclude = fTxtFileExtensionsExclude.getText();
                        if (oldFileExtensionsExclude.contains("MP3;")) {
                            fTxtFileExtensionsExclude.setText(oldFileExtensionsExclude.replace("MP3;", ""));
                        } else if (oldFileExtensionsExclude.contains("mp3;")) {
                            fTxtFileExtensionsExclude.setText(oldFileExtensionsExclude.replace("mp3;", ""));
                        } else if (oldFileExtensionsExclude.contains("MP3")) {
                            fTxtFileExtensionsExclude.setText(oldFileExtensionsExclude.replace("MP3", ""));
                        } else if (oldFileExtensionsExclude.contains("mp3")) {
                            fTxtFileExtensionsExclude.setText(oldFileExtensionsExclude.replace("mp3", ""));
                        }
                    }
                }
            }
        });
        Group grpAutomaticProcessing = new Group(fContainer, SWT.NULL);
        grpAutomaticProcessing.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        grpAutomaticProcessing.setLayout(new GridLayout(2, false));
        grpAutomaticProcessing.setText("Automatic Processing");
        fBtnAutomaticProcessing = new Button(grpAutomaticProcessing, SWT.CHECK);
        fBtnAutomaticProcessing.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fBtnAutomaticProcessing.setText("Automatically process new files using default settings");
        Label lblAutomaticProcessingCountdown = new Label(grpAutomaticProcessing, SWT.NONE);
        lblAutomaticProcessingCountdown.setText("Specify how many seconds to countdown before automatic processing begins:");
        lblAutomaticProcessingCountdown.pack();
        lblAutomaticProcessingCountdown.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTxtAutomaticProcessingCountdown = new Text(grpAutomaticProcessing, SWT.BORDER);
        fTxtAutomaticProcessingCountdown.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                if (!fIsInitializing) {
                    try {
                        Long.parseLong(fTxtAutomaticProcessingCountdown.getText().trim());
                    } catch (NumberFormatException e) {
                        fTxtAutomaticProcessingCountdown.setText("");
                    }
                }
            }
        });
        initControls();
        fIsInitializing = false;
        return fContainer;
    }
