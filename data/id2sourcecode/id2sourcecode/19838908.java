    private void jbInit() {
        setLayout(new FormLayout());
        this.stTopic = new StyledText(this, SWT.BORDER);
        FormData fd_stTopic = new FormData();
        fd_stTopic.top = new FormAttachment(0);
        fd_stTopic.left = new FormAttachment(0);
        fd_stTopic.bottom = new FormAttachment(0, 27);
        fd_stTopic.right = new FormAttachment(100);
        this.stTopic.setLayoutData(fd_stTopic);
        this.tUser = new Tree(this, SWT.BORDER);
        this.tUser.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if (getTUser().getSelectionCount() == 1) {
                    User user = (User) getTUser().getSelection()[0].getData();
                    MessageQueue.addQueue(MessageQueueEnum.MSG_PRIVATE_OUT, new PrivateMessageResponse(Connection.getUserInfo(), user, ""));
                }
            }
        });
        this.tUser.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        FormData fd_tUser = new FormData();
        fd_tUser.right = new FormAttachment(100);
        this.tUser.setLayoutData(fd_tUser);
        this.separator = new Sash(this, SWT.VERTICAL);
        fd_tUser.left = new FormAttachment(separator);
        FormData fd_separator = new FormData();
        fd_separator.bottom = new FormAttachment(100, 0);
        fd_separator.left = new FormAttachment(100, -120);
        fd_separator.right = new FormAttachment(100, -117);
        fd_separator.top = new FormAttachment(this.stTopic, 0);
        this.separator.setLayoutData(fd_separator);
        this.stOutput = new StyledText(this, SWT.BORDER);
        FormData fd_stOutput = new FormData();
        fd_stOutput.top = new FormAttachment(this.stTopic, 0);
        fd_stOutput.right = new FormAttachment(this.separator, 0);
        fd_stOutput.left = new FormAttachment(0);
        this.stOutput.setLayoutData(fd_stOutput);
        this.stInput = new StyledText(this, SWT.BORDER);
        this.stInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == 13) {
                    String cmd = stInput.getText();
                    if (cmd.startsWith("/")) {
                        ScriptManager.ParseCmd(stInput.getText());
                        stInput.setText("");
                    } else {
                        MessageQueue.addQueue(MessageQueueEnum.MSG_CHANNEL_OUT, new ChannelMessageResponse(getChannel(), Connection.getUserInfo(), stInput.getText().replace("\r\n", "")));
                        getEmInput().Clear();
                    }
                }
            }
        });
        fd_stOutput.bottom = new FormAttachment(this.stInput, 0);
        FormData fd_stInput = new FormData();
        fd_stInput.right = new FormAttachment(this.separator);
        fd_stInput.bottom = new FormAttachment(100);
        fd_stInput.left = new FormAttachment(0);
        this.stInput.setLayoutData(fd_stInput);
        setEmInput(new EditorManager(ApplicationInfo.EDT_CHANNEL_INPUT_NAME, stInput));
        setEmOutput(new EditorManager(ApplicationInfo.EDT_CHANNEL_OUTPUT_NAME, stOutput));
        setEmTopic(new EditorManager(ApplicationInfo.EDT_CHANNEL_TOPIC_NAME, stTopic));
        getEmTopic().setEditable(true);
        getEmInput().setEditable(true);
        getEmTopic().addPopupItem("Channel modes", "/com/google/code/cubeirc/resources/img_colors.png", new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ChannelModeForm cmf = new ChannelModeForm(getShell(), SWT.NONE, getChannel());
                cmf.open();
                super.widgetSelected(e);
            }
        });
        setTopic(new ChannelTopicResponse(getChannel(), getChannel().getTopic(), getChannel().getTopicSetter(), getChannel().getTopicTimestamp()));
        this.tSearch = new Text(this, SWT.BORDER);
        FormData fd_tSearch = new FormData();
        fd_tSearch.top = new FormAttachment(stTopic);
        fd_tSearch.left = new FormAttachment(separator);
        this.tSearch.setLayoutData(fd_tSearch);
        this.btnSearch = new Button(this, SWT.FLAT);
        fd_tSearch.bottom = new FormAttachment(btnSearch, 0, SWT.BOTTOM);
        fd_tSearch.right = new FormAttachment(btnSearch);
        this.btnSearch.setImage(SWTResourceManager.getImage(ChannelForm.class, "/com/google/code/cubeirc/resources/img_search.png"));
        FormData fd_btnSearch = new FormData();
        fd_btnSearch.top = new FormAttachment(this.stTopic, 0);
        fd_btnSearch.right = new FormAttachment(this.stTopic, 0, SWT.RIGHT);
        this.btnSearch.setLayoutData(fd_btnSearch);
        getTSearch().setBackground(getEmOutput().getEditor().getBackground());
        getTSearch().setForeground(getEmOutput().getEditor().getForeground());
        updateWho();
        this.lblTotaUsers = new Label(this, SWT.NONE);
        fd_tUser.bottom = new FormAttachment(lblTotaUsers, -4);
        lblTotaUsers.setAlignment(SWT.CENTER);
        FormData fd_lblTotaUsers = new FormData();
        fd_lblTotaUsers.left = new FormAttachment(separator);
        fd_lblTotaUsers.right = new FormAttachment(100);
        fd_lblTotaUsers.bottom = new FormAttachment(separator, 0, SWT.BOTTOM);
        this.lblTotaUsers.setLayoutData(fd_lblTotaUsers);
        this.lblTotaUsers.setText("Tota users:");
        Button btnRefresh = new Button(this, SWT.NONE);
        btnRefresh.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Connection.UpdateUsers(getChannel());
            }
        });
        btnRefresh.setImage(SWTResourceManager.getImage(ChannelForm.class, "/com/google/code/cubeirc/resources/img_refresh.png"));
        fd_tUser.top = new FormAttachment(btnRefresh, 3);
        FormData fd_btnRefresh = new FormData();
        fd_btnRefresh.left = new FormAttachment(separator);
        fd_btnRefresh.right = new FormAttachment(100);
        fd_btnRefresh.top = new FormAttachment(tSearch, 0);
        btnRefresh.setLayoutData(fd_btnRefresh);
        btnRefresh.setText("Refresh");
        this.stTopic.addKeyListener(new ColorsChooserAdapter(getEmTopic()));
        this.stInput.addKeyListener(new ColorsChooserAdapter(getEmInput()));
        this.tUser.addListener(SWT.MenuDetect, new ChannelPopmenuListener(getShell(), getTUser(), getChannel()));
    }
