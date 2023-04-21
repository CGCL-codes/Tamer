    public void run(String[] args) {
        initLog4J(args);
        if (args.length < 1) {
            printGlobalUsage();
        }
        String commandName = args[0];
        String[] newArgs = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            newArgs[i] = args[i + 1];
        }
        CommandProcessor commandProcessor = null;
        try {
            commandProcessor = new CommandProcessor(commandName);
            commandProcessor.init(newArgs);
            String result = commandProcessor.execute();
            System.out.println(result);
            System.exit(0);
        } catch (HelpRequiredException e) {
            commandProcessor.getCommand().printUsage();
            System.exit(0);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] : " + e.getMessage());
            if ((commandProcessor != null) && (commandProcessor.getCommand() != null)) {
                commandProcessor.getCommand().printUsage();
            } else {
                printGlobalUsage();
            }
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not establish connection to VM via JMX : ");
            e.printStackTrace(System.err);
            System.exit(2);
        } catch (Throwable t) {
            System.err.println("There occurred a very unexpected error:");
            t.printStackTrace();
            System.exit(3);
        }
    }
