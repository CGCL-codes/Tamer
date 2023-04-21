    private Desktop initDesktopSupport() {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop = null;
            }
        }
        return desktop;
    }
