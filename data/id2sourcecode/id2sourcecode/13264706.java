    public ClassMemberPane(Composite parent, int style) {
        super(parent, style);
        getShell().addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
                setInfoTipVisible(false);
            }

            public void controlResized(ControlEvent e) {
                setInfoTipVisible(false);
            }
        });
        setLayout(new GridLayout());
        meBar = new ToolBar(this, SWT.NONE);
        meBar.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                meBar_paintControl(e);
            }
        });
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;
        data.widthHint = -1;
        data.heightHint = 40;
        meBar.setLayoutData(data);
        meLabel = new ToolItem(meBar, SWT.DROP_DOWN);
        meLabel.setImage(Images.absent32);
        meLabel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                meLabel_widgetSelected(e);
            }
        });
        meTalk = new ToolItem(meBar, SWT.CHECK);
        meTalk.setEnabled(false);
        meTalk.setImage(Images.talk32);
        meTalk.setToolTipText(Strings.classmembers_talk_text);
        meTalk.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                meTalk_widgetSelected(e);
            }
        });
        meFavActions = new ToolItem(meBar, SWT.DROP_DOWN);
        meFavActions.setImage(Images.favaction32);
        meFavActions.setToolTipText(Strings.classmembers_favactions_text);
        meFavActions.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                meFavActions_widgetSelected(e);
            }
        });
        peerTable = new Table(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.widthHint = -1;
        data.heightHint = -1;
        peerTable.setLayoutData(data);
        peerTable.setHeaderVisible(true);
        imageCol = new TableColumn(peerTable, SWT.NONE);
        imageCol.setWidth(40);
        imageCol.setAlignment(SWT.LEFT);
        selCol = new TableColumn(peerTable, SWT.NONE);
        selCol.setWidth(0);
        selCol.setAlignment(SWT.LEFT);
        nameCol = new TableColumn(peerTable, SWT.NONE);
        nameCol.setWidth(60);
        nameCol.setAlignment(SWT.LEFT);
        peerTable.setSortColumn(nameCol);
        peerTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem ti = peerTable.getSelection()[0];
                PeerDecorator dec = (PeerDecorator) ti.getData();
                PeerBean pbean = dec.getBean();
                ChannelBean cbean = getStore().getChannel();
                updateMenu(cbean.getRole(), pbean.getRole());
            }
        });
        volCol = new TableColumn(peerTable, SWT.NONE);
        volCol.setWidth(70);
        volCol.setAlignment(SWT.LEFT);
        volCol.setResizable(false);
        infoTip = new ToolTip(getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
        warnTip = new ToolTip(getShell(), SWT.BALLOON | SWT.ICON_WARNING);
        errTip = new ToolTip(getShell(), SWT.BALLOON | SWT.ICON_ERROR);
        mPresence = new Menu(getShell(), SWT.POP_UP);
        miReady = new MenuItem(mPresence, SWT.PUSH);
        miReady.setEnabled(false);
        miReady.setText(Strings.classmembers_available_text);
        miReady.setImage(Images.available16);
        miReady.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miReady_widgetSelected(e);
            }
        });
        miBeRightBack = new MenuItem(mPresence, SWT.PUSH);
        miBeRightBack.setEnabled(false);
        miBeRightBack.setText(Strings.classmembers_berightback_text);
        miBeRightBack.setImage(Images.berightback16);
        miBeRightBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miBeRightBack_widgetSelected(e);
            }
        });
        mFavActions = new Menu(getShell(), SWT.POP_UP);
        miDisableMic = new MenuItem(mFavActions, SWT.PUSH);
        miDisableMic.setEnabled(false);
        miDisableMic.setText(Strings.classmembers_disablemic_text);
        miDisableMic.setImage(Images.disablemic16);
        miDisableMic.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miDisableMic_widgetSelected(e);
            }
        });
        miEnableMic = new MenuItem(mFavActions, SWT.PUSH);
        miEnableMic.setEnabled(false);
        miEnableMic.setText(Strings.classmembers_enablemic_text);
        miEnableMic.setImage(Images.enablemic16);
        miEnableMic.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miEnableMic_widgetSelected(e);
            }
        });
        miDisableText = new MenuItem(mFavActions, SWT.PUSH);
        miDisableText.setEnabled(false);
        miDisableText.setText(Strings.classmembers_disabletext_text);
        miDisableText.setImage(Images.disabletext16);
        miDisableText.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miSquelchText_widgetSelected(e);
            }
        });
        miEnableText = new MenuItem(mFavActions, SWT.PUSH);
        miEnableText.setEnabled(false);
        miEnableText.setText(Strings.classmembers_enabletext_text);
        miEnableText.setImage(Images.enabletext16);
        miEnableText.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                miUnsquelchText_widgetSelected(e);
            }
        });
        netSetup();
        storeSetup();
    }
