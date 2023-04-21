    private void loadCommandNotification(Properties properties) {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            return;
        }
        if (Boolean.valueOf(properties.getProperty(IPMonitorProperties.OPTIONS_NOTIFICATION_COMMAND, String.valueOf(IPMonitorProperties.OPTIONS_NOTIFICATION_COMMAND_VALUE)))) {
            ipMonitor.addNotification(CommandNotification.getInstance());
        }
    }
