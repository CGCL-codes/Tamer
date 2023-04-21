    public PrefMenuAction(String text, KeyStroke shortcut, boolean readPrefs, boolean writePrefs) {
        this(text, shortcut);
        setReadPrefs(readPrefs);
        setWritePrefs(writePrefs);
    }
