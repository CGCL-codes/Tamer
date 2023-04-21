    public HyperlinkAction(URI uri, Action desktopAction) {
        super();
        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop API is not " + "supported on the current platform");
        }
        if (desktopAction != Desktop.Action.BROWSE && desktopAction != Desktop.Action.MAIL) {
            throw new IllegalArgumentException("Illegal action type: " + desktopAction + ". Must be BROWSE or MAIL");
        }
        this.desktopAction = desktopAction;
        getURIVisitor();
        setTarget(uri);
    }
