    public RupsMenuBar(Observable observable) {
        this.observable = observable;
        items = new HashMap<String, JMenuItem>();
        fileChooserAction = new FileChooserAction(observable, "Open", PdfFilter.INSTANCE, false);
        MessageAction message = new MessageAction();
        JMenu file = new JMenu(FILE_MENU);
        addItem(file, OPEN, fileChooserAction);
        addItem(file, CLOSE, new FileCloseAction(observable));
        add(file);
        add(Box.createGlue());
        JMenu help = new JMenu(HELP_MENU);
        addItem(help, ABOUT, message);
        addItem(help, VERSIONS, message);
        add(help);
        enableItems(false);
    }
