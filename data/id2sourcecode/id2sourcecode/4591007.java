    protected void doIt(String[] args, CILogonConfigurationDepot configuration) throws Exception {
        String source = null, target = null;
        switch(args.length) {
            case 2:
                target = args[1];
            case 1:
                source = args[0];
            default:
        }
        if (source == null) {
            source = CLITools.prompt("Enter the full path to your configuration file", NONE);
            if (!source.equals(NONE)) {
                target = source;
            }
        }
        boolean isConfigured = false;
        if (source != null && 0 < source.length() && !source.equals("none")) {
            configuration.deserialize(source);
            isConfigured = true;
        }
        doConfig(configuration, isConfigured);
        String doAgain = "y";
        if (target == null || target.length() == 0) {
            target = CLITools.prompt("Enter the name of the file to which I should write the configuration", NONE);
        } else {
            doAgain = CLITools.prompt("I'm getting ready to write the configuration file. Proceed? (y/n)", "y");
        }
        while (doAgain.equals("y")) {
            configuration.save();
            try {
                configuration.serialize(target);
                doAgain = "n";
            } catch (Throwable t) {
                doAgain = CLITools.prompt("Well that didn't work:\"" + t.getMessage() + "\". Try again? (y/n)", "n");
                if (doAgain.equals("y")) {
                    target = CLITools.prompt("Enter the name of the file to which I should write the configuration", target);
                }
            }
            say("done!");
        }
    }
