    @Override
    protected void initMenus() {
        mFileBean = new MenuItem(menuBar, SWT.CASCADE);
        mFileBean.setText(Strings.window_mfile_text);
        mFileBean.setImage(Images.file22);
        mFile = new Menu(shell, SWT.DROP_DOWN);
        mFileBean.setMenu(mFile);
        miLogin = new MenuItem(mFile, SWT.PUSH);
        miLogin.setText(Strings.window_milogin_text);
        miLogin.setImage(Images.login16);
        miLogin.setAccelerator(SWT.CTRL + 'L');
        miLogin.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LoginDialog dialog = App.getApp().getDialog(LoginDialog.class);
                dialog.setVisible(true);
            }
        });
        miLogout = new MenuItem(mFile, SWT.PUSH);
        miLogout.setEnabled(false);
        miLogout.setText(Strings.window_milogout_text);
        miLogout.setImage(Images.logout16);
        miLogout.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LogoutMessage message = new LogoutMessage();
                try {
                    App.getApp().getClient().sendTcp(message);
                    App.getApp().getClient().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        miChannels = new MenuItem(mFile, SWT.PUSH);
        miChannels.setEnabled(false);
        miChannels.setText(Strings.window_michannels_text);
        miChannels.setImage(Images.channels16);
        miChannels.setAccelerator(SWT.ALT + 'C');
        miChannels.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ChannelDialog dialog = App.getApp().getDialog(ChannelDialog.class);
                dialog.setVisible(true);
            }
        });
        miLeaveChannel = new MenuItem(mFile, SWT.PUSH);
        miLeaveChannel.setEnabled(false);
        miLeaveChannel.setText(Strings.window_mileavechannel_text);
        miLeaveChannel.setImage(Images.leavechannel16);
        miLeaveChannel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                leaveCurrentChannel();
            }
        });
        miExit = new MenuItem(mFile, SWT.PUSH);
        miExit.setText(Strings.window_miexit_text);
        miExit.setImage(Images.exit16);
        miExit.setAccelerator(SWT.ALT | SWT.F4);
        miExit.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
        mToolBean = new MenuItem(menuBar, SWT.CASCADE);
        mToolBean.setText(Strings.window_mitools_text);
        mToolBean.setImage(Images.tools22);
        mTool = new Menu(shell, SWT.DROP_DOWN);
        mToolBean.setMenu(mTool);
        miTestVoice = new MenuItem(mTool, SWT.PUSH);
        miTestVoice.setText(Strings.window_mitestvoice_text);
        miTestVoice.setImage(Images.testvoice16);
        miTestVoice.setAccelerator(SWT.ALT + 'v');
        miTestVoice.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TestVoiceDialog.show(shell);
                if (getStore().getChannel() == null) {
                    return;
                }
                getClassMemberPane().setAddOnImage(Images.testvoice16);
            }
        });
        mHelpBean = new MenuItem(menuBar, SWT.CASCADE);
        mHelpBean.setText(Strings.window_mhelp_text);
        mHelpBean.setImage(Images.help22);
        mHelp = new Menu(shell, SWT.DROP_DOWN);
        mHelpBean.setMenu(mHelp);
        miHelp = new MenuItem(mHelp, SWT.PUSH);
        miHelp.setText(Strings.window_mihelp_text);
        miHelp.setImage(Images.help16);
        miAbout = new MenuItem(mHelp, SWT.PUSH);
        miAbout.setText(Strings.window_miabout_text);
        miAbout.setImage(Images.whiteboard16);
        miAbout.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                AboutDialog.show(shell);
            }
        });
        netSetup();
        update();
    }
