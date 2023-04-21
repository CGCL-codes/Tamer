    private void saveCommandNotification(Properties properties) {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            return;
        }
        properties.setProperty(IPMonitorProperties.OPTIONS_NOTIFICATION_COMMAND, String.valueOf(ipMonitor.hasNotification(CommandNotification.getInstance())));
    }
