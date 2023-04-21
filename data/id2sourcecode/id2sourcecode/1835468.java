    Console(CommandParser p, ActionExecutor ae, Macros m) {
        theExecutor = ae;
        theParser = p;
        theMacros = m;
        reader = new BufferedReader(new InputStreamReader(Channels.newInputStream((new FileInputStream(FileDescriptor.in)).getChannel())));
        doContinue = true;
        setName("JJSched console thread");
        setPriority(4);
    }
