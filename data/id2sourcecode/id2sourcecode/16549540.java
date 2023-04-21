    public Object addingService(ServiceReference reference) {
        ConsoleService console = (ConsoleService) bc.getService(reference);
        try {
            consoleSession = console.runSession("console tty", reader, writer);
        } catch (IOException ioe) {
            log(LogService.LOG_ERROR, "Failed to start console session, can not continue");
        }
        return console;
    }
