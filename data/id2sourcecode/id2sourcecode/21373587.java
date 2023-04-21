    public static void addCommand(Command command, boolean overwrite) throws CommandAlreadyLoadedException {
        if (command != null) {
            if (isCommandAvailable(command)) {
                if (overwrite) {
                    commands.remove(command);
                    System.out.println("Command: " + command.getCommandName() + " removed");
                } else {
                    throw new CommandAlreadyLoadedException(command.getCommandName());
                }
            }
            commands.add(command);
            System.out.println("Loaded command: '" + command.getCommandName() + "'");
        }
    }
