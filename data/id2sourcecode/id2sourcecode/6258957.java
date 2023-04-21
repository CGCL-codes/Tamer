    protected void createContents() {
        SystemOutLogger.debug("Creating contents...");
        shell = new Shell();
        shell.setMinimumSize(new Point(555, 605));
        shell.setImage(SWTResourceManager.getImage(APMD5.class, "/images/checksum.ico"));
        shell.setLayout(new FormLayout());
        shell.setSize(555, 605);
        shell.setText("All Purpose MD5");
        shell.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(final DisposeEvent e) {
                SystemOutLogger.debug("Shutting down...");
                SystemOutLogger.debug("Saving options...");
                saveOptions(getFilePath(PROPERTIES_FILE));
                SystemOutLogger.debug("Options Saved.");
                SystemOutLogger.debug("Calling shutdown now...");
                threadPool.shutdownNow();
                SystemOutLogger.debug("Application Terminated.");
            }
        });
        final Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        final MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
        fileMenuItem.setText("File");
        final Menu menu_1 = new Menu(fileMenuItem);
        fileMenuItem.setMenu(menu_1);
        final MenuItem exitMenuItem = new MenuItem(menu_1, SWT.NONE);
        exitMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                shell.dispose();
            }
        });
        exitMenuItem.setText("Exit");
        final MenuItem hashTypeMenuItem = new MenuItem(menu, SWT.CASCADE);
        hashTypeMenuItem.setText("Hash Type");
        final Menu hashTypeSubMenu = new Menu(hashTypeMenuItem);
        hashTypeMenuItem.setMenu(hashTypeSubMenu);
        md5MenuItem = new MenuItem(hashTypeSubMenu, SWT.RADIO);
        md5MenuItem.setSelection(true);
        md5MenuItem.setText("MD5");
        sha1MenuItem = new MenuItem(hashTypeSubMenu, SWT.RADIO);
        sha1MenuItem.setText("SHA-1");
        final MenuItem helpMenuItem = new MenuItem(menu, SWT.CASCADE);
        helpMenuItem.setText("Help");
        final Menu menu_2 = new Menu(helpMenuItem);
        helpMenuItem.setMenu(menu_2);
        final MenuItem viewReadmeMenuItem = new MenuItem(menu_2, SWT.NONE);
        viewReadmeMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                BrowserDialog bd = new BrowserDialog(shell, SWT.NONE, README_URL);
                bd.open();
            }
        });
        viewReadmeMenuItem.setText("View README (online)");
        final MenuItem viewLogMenuItem = new MenuItem(menu_2, SWT.NONE);
        viewLogMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                fLogger.close();
                final String path = getFilePath(LOG_FILE_NAME);
                TextDialog td = new TextDialog(shell);
                td.setTitle("Log File (" + path + ")");
                td.setText(SimpleIO.getText(new File(path)));
                td.open();
            }
        });
        viewLogMenuItem.setText("View Log");
        final MenuItem viewLicenseMenuItem = new MenuItem(menu_2, SWT.NONE);
        viewLicenseMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                TextDialog td = new TextDialog(shell);
                td.setTitle("GPL License v2");
                td.setText(SimpleIO.getText(new File(GPL_FILE)));
                td.open();
            }
        });
        viewLicenseMenuItem.setText("View License");
        final MenuItem aboutMenuItem = new MenuItem(menu_2, SWT.NONE);
        aboutMenuItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                StringBuilder sb = new StringBuilder();
                MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
                mb.setText("About");
                sb.append(MD5Constants.PROGRAM_NAME).append(" is an open source program written by Nick Powers.\n");
                sb.append(MD5Constants.PROGRAM_NAME).append(" is protected by the GNU General Public License v2 (http://www.gnu.org/licenses/gpl.html).\n");
                sb.append("Version: ");
                sb.append(MD5Constants.VERSION);
                sb.append("\nProject Site: ");
                sb.append(MD5Constants.WEBSITE);
                mb.setMessage(sb.toString());
                mb.open();
            }
        });
        aboutMenuItem.setText("About");
        TabFolder tabFolder;
        Composite statusBarComposite;
        tabFolder = new TabFolder(shell, SWT.NONE);
        final FormData fd_tabFolder = new FormData();
        fd_tabFolder.bottom = new FormAttachment(100, -19);
        fd_tabFolder.top = new FormAttachment(0, -1);
        fd_tabFolder.left = new FormAttachment(0, -4);
        fd_tabFolder.right = new FormAttachment(100, 2);
        tabFolder.setLayoutData(fd_tabFolder);
        final TabItem testTabItem = new TabItem(tabFolder, SWT.NONE);
        testTabItem.setText("Test");
        final Composite testTabComposite = new Composite(tabFolder, SWT.NONE);
        testTabItem.setControl(testTabComposite);
        testTabComposite.setLayout(new FormLayout());
        final CLabel calculateAMd5Label_1 = new CLabel(testTabComposite, SWT.NONE);
        FormData formData_5 = new FormData();
        formData_5.right = new FormAttachment(0, 476);
        formData_5.top = new FormAttachment(0);
        formData_5.left = new FormAttachment(0, 88);
        calculateAMd5Label_1.setLayoutData(formData_5);
        calculateAMd5Label_1.setFont(SWTResourceManager.getFont("Arial Unicode MS", 16, SWT.NONE));
        calculateAMd5Label_1.setText("Test a File Against a Checksum");
        final Group testInputGroup = new Group(testTabComposite, SWT.NONE);
        testInputGroup.setLayout(new GridLayout(4, false));
        FormData formData_4 = new FormData();
        formData_4.top = new FormAttachment(calculateAMd5Label_1, 19);
        testInputGroup.setLayoutData(formData_4);
        testInputGroup.setText("Test Input");
        Composite composite_2 = new Composite(testInputGroup, SWT.NONE);
        composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
        final CLabel browseToALabel_4 = new CLabel(composite_2, SWT.NONE);
        browseToALabel_4.setFont(SWTResourceManager.getFont("Arial Unicode MS", 10, SWT.NONE));
        browseToALabel_4.setText("File to be Tested");
        fileToBeTestedHelpLabel = new Label(composite_2, SWT.NONE);
        fileToBeTestedHelpLabel.setToolTipText("Choose a file for which you wish to calculate a checksum. \r\nThe resulting checksum will be tested against either a checksum file\r\n or a checksum you paste below.");
        fileToBeTestedHelpLabel.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        new Label(testInputGroup, SWT.NONE);
        testFileToBeTestedStyledText = new Text(testInputGroup, SWT.BORDER);
        GridData gridData_5 = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
        gridData_5.widthHint = 397;
        testFileToBeTestedStyledText.setLayoutData(gridData_5);
        testFileToBeTestedStyledText.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
        final Button testFileToBeTestedbrowseButton = new Button(testInputGroup, SWT.NONE);
        GridData gridData_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData_3.heightHint = 25;
        gridData_3.widthHint = 82;
        testFileToBeTestedbrowseButton.setLayoutData(gridData_3);
        testFileToBeTestedbrowseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                FileDialog fd = new FileDialog(shell);
                String path = fd.open();
                if (StringUtil.isNotEmpty(path)) {
                    testFileToBeTestedStyledText.setText(path);
                }
            }
        });
        testFileToBeTestedbrowseButton.setText("Browse");
        Composite composite_1 = new Composite(testInputGroup, SWT.NONE);
        composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        composite_1.setLayout(new RowLayout(SWT.HORIZONTAL));
        final CLabel browseToALabel_4_1 = new CLabel(composite_1, SWT.NONE);
        browseToALabel_4_1.setFont(SWTResourceManager.getFont("Arial Unicode MS", 10, SWT.NONE));
        browseToALabel_4_1.setText("Checksum File");
        checkSumFileHelpLabel = new Label(composite_1, SWT.NONE);
        checkSumFileHelpLabel.setToolTipText("You may choose a text file that includes a checksum.\r\nThat file will be opened and read and tested against\r\nthe file chosen above.");
        checkSumFileHelpLabel.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        new Label(testInputGroup, SWT.NONE);
        testMd5FileStyledText = new Text(testInputGroup, SWT.BORDER);
        GridData gridData_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
        gridData_1.widthHint = 397;
        testMd5FileStyledText.setLayoutData(gridData_1);
        testMd5FileStyledText.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
        final Button testMd5FileBrowseButton = new Button(testInputGroup, SWT.NONE);
        GridData gridData_4 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gridData_4.heightHint = 25;
        gridData_4.widthHint = 82;
        testMd5FileBrowseButton.setLayoutData(gridData_4);
        testMd5FileBrowseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                FileDialog fd = new FileDialog(shell);
                String path = fd.open();
                if (StringUtil.isNotEmpty(path)) {
                    testMd5FileStyledText.setText(path);
                }
            }
        });
        testMd5FileBrowseButton.setText("Browse");
        Composite composite = new Composite(testInputGroup, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));
        final CLabel browseToALabel_4_1_1 = new CLabel(composite, SWT.NONE);
        browseToALabel_4_1_1.setFont(SWTResourceManager.getFont("Arial Unicode MS", 10, SWT.NONE));
        browseToALabel_4_1_1.setText("Or, Paste or type the Checksum");
        checkSumStringHelpLabel = new Label(composite, SWT.NONE);
        checkSumStringHelpLabel.setToolTipText("You may paste or type in a checksum to be tested against\n the generated checksum from the \"File to be Tested\" above.");
        checkSumStringHelpLabel.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        new Label(testInputGroup, SWT.NONE);
        testPasteTypeMd5styledText = new Text(testInputGroup, SWT.BORDER);
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gridData.widthHint = 397;
        testPasteTypeMd5styledText.setLayoutData(gridData);
        testPasteTypeMd5styledText.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
        new Label(testInputGroup, SWT.NONE);
        Composite composite_6 = new Composite(testInputGroup, SWT.NONE);
        GridData gridData_8_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1);
        gridData_8_1.widthHint = 175;
        composite_6.setLayoutData(gridData_8_1);
        composite_6.setLayout(new RowLayout(SWT.HORIZONTAL));
        testButton = new Button(composite_6, SWT.NONE);
        testButton.setLayoutData(new RowData(120, 25));
        testButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (isExecuting.get()) {
                    sLogger.logAppend(" (can't stop now)");
                    return;
                }
                SystemOutLogger.debug("Testing a checksum...");
                testResultStyledText.setText(StringUtil.EMPTY_STRING);
                setStatusIcon(null);
                final String checksumFilePath = trim(testMd5FileStyledText.getText());
                final String filePathToBeTested = trim(testFileToBeTestedStyledText.getText());
                final String md5String = trim(testPasteTypeMd5styledText.getText());
                if (isEmpty(filePathToBeTested)) {
                    sLogger.logWarn("No file specified to test");
                    return;
                }
                final File fileToBeTested = new File(filePathToBeTested);
                if (!fileToBeTested.exists()) {
                    sLogger.logWarn("The file to be tested does not exist");
                    return;
                }
                if (isEmpty(checksumFilePath) && isEmpty(md5String)) {
                    sLogger.logWarn("No MD5 specified to test the file");
                    return;
                }
                isExecuting.set(true);
                threadPool.submit(progressBarWorker);
                SystemOutLogger.debug("Checksum Type:" + getChecksumCalculator());
                TestAChecksum worker = null;
                if (isNotEmpty(checksumFilePath)) {
                    worker = new TestAChecksum(apmd5, getChecksumCalculator(), fileToBeTested, new File(checksumFilePath));
                } else {
                    worker = new TestAChecksum(apmd5, getChecksumCalculator(), fileToBeTested, md5String);
                }
                Future<ChecksumTestResult> future = threadPool.submit(worker);
                threadPool.submit(new PopulateChecksum(apmd5, future));
            }
        });
        testButton.setText("Test");
        testStatusIcon = new Label(composite_6, SWT.NONE);
        testStatusIcon.setLayoutData(new RowData(32, 32));
        final Group testResultGroup = new Group(testTabComposite, SWT.NONE);
        formData_4.bottom = new FormAttachment(testResultGroup, -6);
        formData_4.right = new FormAttachment(testResultGroup, 0, SWT.RIGHT);
        formData_4.left = new FormAttachment(testResultGroup, 0, SWT.LEFT);
        FormData formData_3 = new FormData();
        formData_3.top = new FormAttachment(0, 320);
        formData_3.bottom = new FormAttachment(100, -10);
        formData_3.right = new FormAttachment(100, -10);
        formData_3.left = new FormAttachment(0, 10);
        testResultGroup.setLayoutData(formData_3);
        testResultGroup.setText("Test Result");
        FillLayout fillLayout_1 = new FillLayout(SWT.HORIZONTAL);
        fillLayout_1.marginHeight = 4;
        fillLayout_1.marginWidth = 4;
        testResultGroup.setLayout(fillLayout_1);
        testResultStyledText = new Text(testResultGroup, SWT.V_SCROLL | SWT.BORDER);
        testResultStyledText.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NONE));
        final TabItem calculateTabItem = new TabItem(tabFolder, SWT.NONE);
        calculateTabItem.setText("Calculate");
        final Composite calculateTabComposite = new Composite(tabFolder, SWT.NONE);
        calculateTabItem.setControl(calculateTabComposite);
        calculateTabComposite.setLayout(new FormLayout());
        final CLabel calculateAMd5Label = new CLabel(calculateTabComposite, SWT.NONE);
        FormData formData_2 = new FormData();
        formData_2.bottom = new FormAttachment(0, 34);
        formData_2.top = new FormAttachment(0);
        formData_2.left = new FormAttachment(0, 157);
        formData_2.right = new FormAttachment(100, -78);
        calculateAMd5Label.setLayoutData(formData_2);
        calculateAMd5Label.setFont(SWTResourceManager.getFont("Arial Unicode MS", 16, SWT.NONE));
        calculateAMd5Label.setText("Calculate A Checksum");
        calculateInputGroup = new Group(calculateTabComposite, SWT.NONE);
        calculateInputGroup.setLayout(new GridLayout(3, false));
        FormData formData = new FormData();
        formData.top = new FormAttachment(calculateAMd5Label, 6);
        formData.right = new FormAttachment(100, -14);
        formData.left = new FormAttachment(0, 10);
        calculateInputGroup.setLayoutData(formData);
        calculateInputGroup.setText("Calculate Input");
        Composite composite_5 = new Composite(calculateInputGroup, SWT.NONE);
        composite_5.setLayout(new GridLayout(6, false));
        chooseAFileRadioButton = new Button(composite_5, SWT.RADIO);
        chooseAFileRadioButton.setSelection(true);
        chooseAFileRadioButton.setText("Choose a File");
        chooseAFileDirHelpLabel = new Label(composite_5, SWT.NONE);
        chooseAFileDirHelpLabel.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        chooseAFileDirHelpLabel.setToolTipText("Choose either a file or a directory to calculate a checksum\nor a list of checksums (if a directory is chosen).");
        new Label(composite_5, SWT.NONE);
        new Label(composite_5, SWT.NONE);
        new Label(composite_5, SWT.NONE);
        recurseDirectoriesButton = new Button(composite_5, SWT.CHECK);
        recurseDirectoriesButton.setToolTipText("Recursively look at each folder and calculate a checksum for every file.");
        recurseDirectoriesButton.setText("Recurse Directories?");
        chooseADirectoryRadioButton = new Button(composite_5, SWT.RADIO);
        chooseADirectoryRadioButton.setText("Choose a Directory");
        new Label(composite_5, SWT.NONE);
        new Label(composite_5, SWT.NONE);
        new Label(composite_5, SWT.NONE);
        new Label(composite_5, SWT.NONE);
        createFileButton = new Button(composite_5, SWT.CHECK);
        createFileButton.setToolTipText("Create a file of the generated checksums with checksum and file name.");
        createFileButton.setText("Create a file?");
        new Label(calculateInputGroup, SWT.NONE);
        new Label(calculateInputGroup, SWT.NONE);
        Composite composite_4 = new Composite(calculateInputGroup, SWT.NONE);
        composite_4.setLayout(new RowLayout(SWT.HORIZONTAL));
        final CLabel browseToALabel = new CLabel(composite_4, SWT.NONE);
        browseToALabel.setFont(SWTResourceManager.getFont("Arial Unicode MS", 10, SWT.NONE));
        browseToALabel.setText("Browse to a file or Directory");
        fileToBeTestedHelpLabel_1 = new Label(composite_4, SWT.NONE);
        fileToBeTestedHelpLabel_1.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        fileToBeTestedHelpLabel_1.setToolTipText("Select either a file or a directory. If a directory is chosen, consider recusivley \ncalculating checksums by checking the box to the right.");
        new Label(calculateInputGroup, SWT.NONE);
        new Label(calculateInputGroup, SWT.NONE);
        calculateBrowseStyledText = new Text(calculateInputGroup, SWT.BORDER);
        GridData gridData_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData_2.widthHint = 380;
        calculateBrowseStyledText.setLayoutData(gridData_2);
        calculateBrowseStyledText.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
        new Label(calculateInputGroup, SWT.NONE);
        final Button calculateBrowseButton = new Button(calculateInputGroup, SWT.NONE);
        GridData gridData_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData_6.heightHint = 25;
        gridData_6.widthHint = 82;
        calculateBrowseButton.setLayoutData(gridData_6);
        calculateBrowseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String path = EMPTY_STRING;
                if (chooseAFileRadioButton.getSelection()) {
                    FileDialog fd = new FileDialog(shell);
                    path = fd.open();
                } else {
                    DirectoryDialog dd = new DirectoryDialog(shell);
                    path = dd.open();
                }
                if (StringUtil.isNotEmpty(path)) {
                    calculateBrowseStyledText.setText(path);
                }
            }
        });
        calculateBrowseButton.setText("Browse");
        Composite composite_3 = new Composite(calculateInputGroup, SWT.NONE);
        composite_3.setLayout(new RowLayout(SWT.HORIZONTAL));
        final CLabel browseToALabel_1 = new CLabel(composite_3, SWT.NONE);
        browseToALabel_1.setFont(SWTResourceManager.getFont("Arial Unicode MS", 10, SWT.NONE));
        browseToALabel_1.setText("Or, Type in a string of characters");
        fileToBeTestedHelpLabel_3 = new Label(composite_3, SWT.NONE);
        fileToBeTestedHelpLabel_3.setImage(SWTResourceManager.getImage(APMD5.class, "/images/question-16.png"));
        fileToBeTestedHelpLabel_3.setToolTipText("Type in a string in order to get a checksum for it.\n This is useful for getting a hash of a password for testing or storage.");
        new Label(calculateInputGroup, SWT.NONE);
        new Label(calculateInputGroup, SWT.NONE);
        calculateStringStyledText = new Text(calculateInputGroup, SWT.BORDER);
        GridData gridData_7 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData_7.widthHint = 380;
        calculateStringStyledText.setLayoutData(gridData_7);
        calculateStringStyledText.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
        new Label(calculateInputGroup, SWT.NONE);
        final Group calculateOutputGroup = new Group(calculateTabComposite, SWT.NONE);
        formData.bottom = new FormAttachment(calculateOutputGroup, -6);
        new Label(calculateInputGroup, SWT.NONE);
        Composite composite_7 = new Composite(calculateInputGroup, SWT.NONE);
        composite_7.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        composite_7.setLayout(new RowLayout(SWT.HORIZONTAL));
        calculateButton = new Button(composite_7, SWT.NONE);
        calculateButton.setLayoutData(new RowData(120, 25));
        calculateButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (isExecuting.get()) {
                    sLogger.logAppend(" (can't stop now)");
                    return;
                }
                String calculateFileOrDirPath = trim(calculateBrowseStyledText.getText());
                String string = trim(calculateStringStyledText.getText());
                boolean calcAString = isNotEmpty(string);
                if (isEmpty(calculateFileOrDirPath) && isEmpty(string)) {
                    sLogger.logWarn("You did not choose a file or enter a string");
                    return;
                }
                final File calculateFileOrDirFile = new File(calculateFileOrDirPath);
                if (isNotEmpty(calculateFileOrDirPath) && !calculateFileOrDirFile.exists()) {
                    sLogger.logWarn("The file you chose does not exist");
                    return;
                }
                SystemOutLogger.debug("Checksum Type:" + getChecksumCalculator());
                caculateResultStyledText.setText("");
                CalculateAChecksum worker = null;
                if (calcAString) {
                    worker = new CalculateAChecksum(apmd5, getChecksumCalculator(), string);
                } else {
                    File saveFile = null;
                    if (createFileButton.getSelection()) {
                        FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
                        String savePath = saveDialog.open();
                        if (isEmpty(savePath)) {
                            sLogger.logWarn("You did not choose a file to output the checksum(s)");
                            return;
                        }
                        saveFile = new File(savePath);
                        if (saveFile.exists()) {
                            MessageBox mb = new MessageBox(shell, SWT.YES | SWT.NO);
                            mb.setText("File Already Exists");
                            mb.setMessage("File " + saveFile.getName() + " already exists\n Overwrite?");
                            if (mb.open() == SWT.NO) {
                                sLogger.logWarn("Chose not to overwrite the file. Did not create checksum(s)");
                                return;
                            }
                        }
                    }
                    worker = new CalculateAChecksum(apmd5, getChecksumCalculator(), calculateFileOrDirFile, saveFile, recurseDirectoriesButton.getSelection());
                }
                isExecuting.set(true);
                cancelButton.setVisible(true);
                threadPool.submit(progressBarWorker);
                threadPool.submit(worker);
            }
        });
        calculateButton.setText("Calculate");
        cancelButton = new Button(composite_7, SWT.NONE);
        cancelButton.setVisible(false);
        cancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isExecuting.set(false);
                sLogger.log("Cancelled the current operation");
            }
        });
        cancelButton.setToolTipText("Cancel the current operation");
        cancelButton.setText("Cancel");
        FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
        fillLayout.marginWidth = 4;
        fillLayout.marginHeight = 4;
        calculateOutputGroup.setLayout(fillLayout);
        FormData formData_1 = new FormData();
        formData_1.top = new FormAttachment(0, 290);
        formData_1.bottom = new FormAttachment(100, -10);
        formData_1.right = new FormAttachment(100, -14);
        formData_1.left = new FormAttachment(0, 10);
        calculateOutputGroup.setLayoutData(formData_1);
        calculateOutputGroup.setText("Calculate Result");
        caculateResultStyledText = new Text(calculateOutputGroup, SWT.V_SCROLL | SWT.BORDER);
        caculateResultStyledText.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NONE));
        statusBarComposite = new Composite(shell, SWT.NONE);
        statusBarComposite.setLayout(new FormLayout());
        final FormData fd_statusBarComposite = new FormData();
        fd_statusBarComposite.top = new FormAttachment(0, 528);
        fd_statusBarComposite.bottom = new FormAttachment(100, -1);
        fd_statusBarComposite.left = new FormAttachment(0, -2);
        fd_statusBarComposite.right = new FormAttachment(100, 2);
        statusBarComposite.setLayoutData(fd_statusBarComposite);
        messageStyledText = new StyledText(statusBarComposite, SWT.BORDER);
        final FormData fd_messageStyledText = new FormData();
        fd_messageStyledText.bottom = new FormAttachment(100, -1);
        fd_messageStyledText.top = new FormAttachment(0, 3);
        fd_messageStyledText.right = new FormAttachment(100, -143);
        fd_messageStyledText.left = new FormAttachment(0, 3);
        messageStyledText.setLayoutData(fd_messageStyledText);
        messageStyledText.setEditable(false);
        progressBar = new ProgressBar(statusBarComposite, SWT.SMOOTH);
        final FormData fd_progressBar = new FormData();
        fd_progressBar.bottom = new FormAttachment(100, -1);
        fd_progressBar.top = new FormAttachment(0, 3);
        fd_progressBar.right = new FormAttachment(100, -5);
        fd_progressBar.left = new FormAttachment(messageStyledText, 3, SWT.DEFAULT);
        progressBar.setLayoutData(fd_progressBar);
    }
