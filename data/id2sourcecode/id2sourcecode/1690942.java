    public DesktopSupport() {
        try {
            MySeriesLogger.logger.log(Level.INFO, "Checking desktop support");
            desktop = Desktop.getDesktop();
            desktopSupport = Desktop.isDesktopSupported();
            browseSupport = desktop.isSupported(Desktop.Action.BROWSE);
            mailSupport = desktop.isSupported(Desktop.Action.MAIL);
        } catch (UnsupportedOperationException ex) {
            MySeriesLogger.logger.log(Level.WARNING, "Desktop is not supported in the current OS");
        }
    }
