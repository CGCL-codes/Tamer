    public void open(Shell parent) {
        final int[][] channelRouting = getChannelRoutingSettings();
        final int[][][] programRouting = getProgramRoutingSettings();
        final Shell dialog = DialogUtils.newDialog(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        dialog.setText(TuxGuitar.getProperty("jack.settings.dialog"));
        dialog.setLayout(new GridLayout());
        dialog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final TabFolder tabFolder = new TabFolder(dialog, SWT.TOP);
        tabFolder.setLayout(new FormLayout());
        final TabItem[] routingTabs = new TabItem[2];
        int synthRouteType = this.settings.getConfig().getIntConfigValue("jack.midi.ports.type", JackOutputPortRouter.CREATE_UNIQUE_PORT);
        Composite tabControl = new Composite(tabFolder, SWT.NONE);
        tabControl.setLayout(new GridLayout());
        tabControl.setLayoutData(new FormData(TAB_WIDTH, TAB_HEIGHT));
        TabItem tabItem = new TabItem(tabFolder, SWT.None);
        tabItem.setText(TuxGuitar.getProperty("jack.settings.dialog.options"));
        tabItem.setControl(tabControl);
        Group groupSynth = new Group(tabControl, SWT.SHADOW_ETCHED_IN);
        groupSynth.setLayout(new GridLayout(1, false));
        groupSynth.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupSynth.setText(TuxGuitar.getProperty("jack.settings.dialog.options.midi-port"));
        final Button buttonSynthRouteType1 = new Button(groupSynth, SWT.RADIO);
        final Button buttonSynthRouteType2 = new Button(groupSynth, SWT.RADIO);
        final Button buttonSynthRouteType3 = new Button(groupSynth, SWT.RADIO);
        buttonSynthRouteType1.setText(TuxGuitar.getProperty("jack.settings.dialog.options.midi-port.type.single"));
        buttonSynthRouteType2.setText(TuxGuitar.getProperty("jack.settings.dialog.options.midi-port.type.multiple-by-channel"));
        buttonSynthRouteType3.setText(TuxGuitar.getProperty("jack.settings.dialog.options.midi-port.type.multiple-by-program"));
        if (synthRouteType == JackOutputPortRouter.CREATE_MULTIPLE_PORTS_BY_CHANNEL) {
            buttonSynthRouteType2.setSelection(true);
            routingTabs[0] = openChannelRoutingTab(tabFolder, channelRouting);
        } else if (synthRouteType == JackOutputPortRouter.CREATE_MULTIPLE_PORTS_BY_PROGRAM) {
            buttonSynthRouteType3.setSelection(true);
            routingTabs[1] = openProgramRoutingTab(tabFolder, programRouting);
        } else {
            buttonSynthRouteType1.setSelection(true);
        }
        final SelectionListener routeTypeListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                boolean channelRoutingTabsEnabled = buttonSynthRouteType2.getSelection();
                boolean programRoutingTabsEnabled = buttonSynthRouteType3.getSelection();
                if (channelRoutingTabsEnabled && routingTabs[0] == null) {
                    routingTabs[0] = openChannelRoutingTab(tabFolder, channelRouting);
                } else if (!channelRoutingTabsEnabled && routingTabs[0] != null) {
                    routingTabs[0].dispose();
                    routingTabs[0] = null;
                }
                if (programRoutingTabsEnabled && routingTabs[1] == null) {
                    routingTabs[1] = openProgramRoutingTab(tabFolder, programRouting);
                } else if (!programRoutingTabsEnabled && routingTabs[1] != null) {
                    routingTabs[1].dispose();
                    routingTabs[1] = null;
                }
            }
        };
        buttonSynthRouteType1.addSelectionListener(routeTypeListener);
        buttonSynthRouteType2.addSelectionListener(routeTypeListener);
        buttonSynthRouteType3.addSelectionListener(routeTypeListener);
        Composite compositeButtons = new Composite(dialog, SWT.NONE);
        compositeButtons.setLayout(new GridLayout(2, false));
        compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true));
        final Button buttonOK = new Button(compositeButtons, SWT.PUSH);
        buttonOK.setText(TuxGuitar.getProperty("ok"));
        buttonOK.setLayoutData(getButtonData());
        buttonOK.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                int type = JackOutputPortRouter.CREATE_UNIQUE_PORT;
                if (buttonSynthRouteType2.getSelection()) {
                    type = JackOutputPortRouter.CREATE_MULTIPLE_PORTS_BY_CHANNEL;
                } else if (buttonSynthRouteType3.getSelection()) {
                    type = JackOutputPortRouter.CREATE_MULTIPLE_PORTS_BY_PROGRAM;
                }
                saveChanges(type, channelRouting, programRouting);
                dialog.dispose();
            }
        });
        Button buttonCancel = new Button(compositeButtons, SWT.PUSH);
        buttonCancel.setText(TuxGuitar.getProperty("cancel"));
        buttonCancel.setLayoutData(getButtonData());
        buttonCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                dialog.dispose();
            }
        });
        dialog.setDefaultButton(buttonOK);
        DialogUtils.openDialog(dialog, DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
    }
