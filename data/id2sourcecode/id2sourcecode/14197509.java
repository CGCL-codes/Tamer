    public HostConfiguration(Composite parent, int style) throws PentahoAdminXMLException {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        final Label solutionPathLabel = new Label(this, SWT.NONE);
        solutionPathLabel.setText(Messages.getString("HostConfiguration.solutionpath"));
        solutionPathText = new Text(this, SWT.BORDER);
        final GridData gd_solutionPathText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        solutionPathText.setLayoutData(gd_solutionPathText);
        final Button solutionBrowseButton = new Button(this, SWT.NONE);
        solutionBrowseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                DirectoryDialog dlg = new DirectoryDialog(getShell());
                dlg.setFilterPath(getSolutionPathText());
                dlg.setText(Messages.getString("HostConfiguration.selectlocation"));
                dlg.setMessage(Messages.getString("HostConfiguration.selectdirectory"));
                String dir = dlg.open();
                if (dir != null) {
                    setSolutionPathText(dir);
                }
            }
        });
        solutionBrowseButton.setText(Messages.getString("HostConfiguration.selectbrowse"));
        final Label hostNameLabel = new Label(this, SWT.NONE);
        hostNameLabel.setText(Messages.getString("HostConfiguration.hostname"));
        hostNametext = new Text(this, SWT.BORDER);
        hostNametext.setBackground(SWTResourceManager.getColor(255, 0, 0));
        final GridData gd_hostNametext = new GridData(SWT.FILL, SWT.CENTER, true, false);
        hostNametext.setLayoutData(gd_hostNametext);
        new Label(this, SWT.NONE);
        final Label localeLanguageLabel = new Label(this, SWT.NONE);
        localeLanguageLabel.setText(Messages.getString("HostConfiguration.localelanguage"));
        languageCombo = new Combo(this, SWT.NONE);
        final GridData gd_languageCombo = new GridData(SWT.FILL, SWT.CENTER, true, false);
        languageCombo.setLayoutData(gd_languageCombo);
        languageCombo.setItems(getLocaleLanguage());
        new Label(this, SWT.NONE);
        final Label localeCountryLabel = new Label(this, SWT.NONE);
        localeCountryLabel.setText(Messages.getString("HostConfiguration.localecountry"));
        countryCombo = new Combo(this, SWT.NONE);
        final GridData gd_countryCombo = new GridData(SWT.FILL, SWT.CENTER, true, false);
        countryCombo.setLayoutData(gd_countryCombo);
        countryCombo.setItems(getLocaleCountry());
        new Label(this, SWT.NONE);
        final Label encodingLabel = new Label(this, SWT.NONE);
        encodingLabel.setText(Messages.getString("HostConfiguration.encoding"));
        encodingText = new Text(this, SWT.BORDER);
        final GridData gd_encodingText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        encodingText.setLayoutData(gd_encodingText);
        new Label(this, SWT.NONE);
        final Label authenticationMethodLabel = new Label(this, SWT.NONE);
        authenticationMethodLabel.setText(Messages.getString("HostConfiguration.authmethod"));
        final Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new FillLayout());
        final Button simpleButton = new Button(composite, SWT.RADIO);
        simpleButton.setText(Messages.getString("HostConfiguration.authsimple"));
        final Button jndiButton = new Button(composite, SWT.RADIO);
        jndiButton.setText(Messages.getString("HostConfiguration.authjndi"));
        final Button ldapButton = new Button(composite, SWT.RADIO);
        ldapButton.setText(Messages.getString("HostConfiguration.authldap"));
        new Label(this, SWT.NONE);
        final Composite composite_1 = new Composite(this, SWT.NONE);
        composite_1.setLayout(new FillLayout());
        saveButton = new Button(composite_1, SWT.BUTTON1);
        saveButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                saveFile();
            }
        });
        saveButton.setEnabled(false);
        saveButton.setText(Messages.getString("HostConfiguration.savefile"));
        copyOriginalButton = new Button(composite_1, SWT.BUTTON1);
        copyOriginalButton.setEnabled(true);
        copyOriginalButton.setText(Messages.getString("HostConfiguration.revertfile"));
        copyOriginalButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                FileUtils.copyFile("web.xml", filename);
            }
        });
        solutionPathText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
                setSaveButton(true);
            }
        });
        hostNametext.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
                setSaveButton(true);
                Boolean ret = checkUrl(getHostNameText());
                if (ret == true) {
                    setHostBackgroundColor(255, 255, 255);
                } else setHostBackgroundColor(255, 0, 0);
            }
        });
        countryCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
                setSaveButton(true);
            }
        });
        encodingText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
                setSaveButton(true);
            }
        });
        new Label(this, SWT.NONE);
        init();
    }
