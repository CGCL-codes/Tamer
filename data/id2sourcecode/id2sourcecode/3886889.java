    public CommandAlreadyLoadedException(String commandName) {
        super("Command was already loaded: " + commandName + "\nUse overwrite parameter");
    }
