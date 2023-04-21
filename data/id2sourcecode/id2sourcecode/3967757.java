    public BrowserLaunchHyperlinkListener(final FormattedUpdate formatted_update) {
        Desktop d = null;
        if (Desktop.isDesktopSupported()) {
            d = Desktop.getDesktop();
            if (!d.isSupported(Desktop.Action.BROWSE)) {
                d = null;
            }
        }
        desktop = d;
        update = formatted_update;
    }
