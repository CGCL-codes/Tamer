    private static JMenuBar createHandballMenu(HandballModel handballModel) {
        JMenuBar mbar;
        JMenu menuDatei, menuHilfe, menuEinstellungen;
        mbar = new JMenuBar();
        menuDatei = JMenuHelper.addMenuBarItem(mbar, Resources.getString("menu.file"));
        menuEinstellungen = JMenuHelper.addMenuBarItem(mbar, Resources.getString("menu.settings"));
        menuHilfe = JMenuHelper.addMenuBarItem(mbar, Resources.getString("menu.about"));
        JMenuHelper.addMenuItem(menuDatei, "", new NewAction(handballModel));
        JMenuHelper.addMenuItem(menuDatei, "", new OpenAction(handballModel));
        JMenuHelper.addMenuItem(menuDatei, "-");
        saveAction = new SaveAction(handballModel);
        JMenuHelper.addMenuItem(menuDatei, "", saveAction);
        JMenuHelper.addMenuItem(menuDatei, "-");
        setNameAction = new SetMoveNameAction(handballModel);
        JMenuHelper.addMenuItem(menuDatei, "", setNameAction);
        JMenuHelper.addMenuItem(menuDatei, "-");
        JMenuHelper.addMenuItem(menuDatei, "", new PrintActualSequenzAction(handballModel));
        JMenuHelper.addMenuItem(menuDatei, "", new PrintMoveAction(handballModel));
        JMenuHelper.addMenuItem(menuDatei, "", new CreateMovePdfAction(handballModel));
        JMenuHelper.addMenuItem(menuDatei, "-");
        closeAction = new CloseAction(handballModel);
        JMenuHelper.addMenuItem(menuDatei, "", closeAction);
        JMenuHelper.addMenuItem(menuEinstellungen, "", new ChangeColorsAction());
        JMenuHelper.addMenuItem(menuHilfe, "", new AboutAction());
        return mbar;
    }
