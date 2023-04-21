    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("The first argument must be a command.");
            usage();
            System.exit(-1);
        }
        String commandName = args[0];
        Command command = commands.get(commandName);
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            usage();
            System.exit(-1);
        }
        String[] commandArgs = new String[args.length - 1];
        for (int i = 0; i < commandArgs.length; i++) {
            commandArgs[i] = args[i + 1];
        }
        Object options = command.getOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(commandArgs);
            command.validate();
            command.execute();
            System.exit(0);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            usage(parser, commandName);
            System.exit(1);
        } catch (CommandException e) {
            System.err.print(e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println(String.format(" (%s)", cause.getMessage()));
            } else {
                System.out.println();
            }
            if (e.isUsageError()) {
                usage(parser, commandName);
            }
            System.exit(2);
        } catch (RuntimeException e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            System.exit(3);
        }
    }
