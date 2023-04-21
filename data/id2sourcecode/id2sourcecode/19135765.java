    void executeCommand(String[] cmd) {
        boolean result;
        CommandHandler handler;
        if (cmd[0].equals(helpCommand)) {
            if (cmd.length == 1) printAvailableCommands(); else {
                for (int i = 0; i < handlers.size(); i++) {
                    handler = (CommandHandler) handlers.elementAt(i);
                    if (handler.getCommandName().equals(cmd[1])) {
                        handler.printCommandHelp(System.out, CommandHandler.longHelp);
                    }
                }
            }
            return;
        }
        for (int i = 0; i < handlers.size(); i++) {
            handler = (CommandHandler) handlers.elementAt(i);
            if (handler.getCommandName().equals(cmd[0])) {
                String[] cmdArgs = new String[cmd.length - 1];
                for (int j = 0; j < cmdArgs.length; j++) cmdArgs[j] = cmd[j + 1];
                try {
                    System.out.println();
                    result = handler.processCommand(cmdArgs, orb, System.out);
                    if (result == CommandHandler.parseError) {
                        handler.printCommandHelp(System.out, CommandHandler.longHelp);
                    }
                    System.out.println();
                } catch (Exception ex) {
                }
                return;
            }
        }
        printAvailableCommands();
    }
