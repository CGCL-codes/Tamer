    public void doListThread(String command, String[] args) {
        if (args.length == 0) {
            jdp_console.writeOutput(user.listAllThreads(true));
        } else if (args.length == 1) {
            if (args[0].equals("all")) {
                jdp_console.writeOutput(user.listAllThreads(false));
            } else if (args[0].equals("byname")) {
                jdp_console.writeOutput(user.listAllThreads(true));
            } else if (args[0].equals("ready")) {
                jdp_console.writeOutput(user.listReadyThreads());
            } else if (args[0].equals("wakeup")) {
                jdp_console.writeOutput(user.listWakeupThreads());
            } else if (args[0].equals("run")) {
                if (Platform.listtRunImplemented == 1) {
                    jdp_console.writeOutput(user.listRunThreads());
                } else {
                    jdp_console.writeOutput("Sorry, listt run is not implemented yet on this platform");
                }
            } else if (args[0].equals("system")) {
                if (Platform.listtSystemImplemented == 1) {
                    jdp_console.writeOutput(user.listSystemThreads());
                } else {
                    jdp_console.writeOutput("Sorry, listt system is not implemented yet on this platform");
                }
            } else if (args[0].equals("gc")) {
                jdp_console.writeOutput(user.listGCThreads());
            } else {
                printHelp(command);
            }
        } else {
            printHelp(command);
        }
    }
