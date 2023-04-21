    @SuppressWarnings("unused")
    @Override
    public void handleEvent(Event event) {
        if (getTSender().getSelection().length != 0) {
            TreeItem it = getTSender().getSelection()[0];
            if (it.getData() instanceof User) {
                final User sUser = (User) it.getData();
                final ChannelPopupListener cpl = new ChannelPopupListener(getShell(), sUser, getChannel());
                Menu menu = new Menu(getTSender());
                MenuItem mi = new MenuItem(menu, SWT.NORMAL);
                mi.setText("//" + sUser.getNick());
                mi.setEnabled(false);
                addMenuItem(menu, SWT.NORMAL, "Send private message", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_message.png"), cpl);
                MenuItem sep3 = new MenuItem(menu, SWT.SEPARATOR);
                addMenuItem(menu, SWT.NORMAL, "DCC Send file", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_sendfile.png"), cpl);
                addMenuItem(menu, SWT.NORMAL, "DCC Chat", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_sendfile.png"), cpl);
                MenuItem sep4 = new MenuItem(menu, SWT.SEPARATOR);
                addMenuItem(menu, SWT.NORMAL, "Op", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_op.png"), cpl);
                addMenuItem(menu, SWT.NORMAL, "DeOp", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_deop.png"), cpl);
                MenuItem sep1 = new MenuItem(menu, SWT.SEPARATOR);
                addMenuItem(menu, SWT.NORMAL, "Voice", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_op.png"), cpl);
                addMenuItem(menu, SWT.NORMAL, "DeVoice", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_deop.png"), cpl);
                MenuItem sep2 = new MenuItem(menu, SWT.SEPARATOR);
                addMenuItem(menu, SWT.NORMAL, "Kick", SWTResourceManager.getImage(Main.class, "/com/google/code/cubeirc/resources/img_trash.png"), cpl);
                menu.setLocation(getMousePosition());
                menu.setVisible(true);
            }
        }
    }
