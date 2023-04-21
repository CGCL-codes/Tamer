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
                jdp_console.writeOutput(user.listRunThreads());
            } else if (args[0].equals("system")) {
                jdp_console.writeOutput(user.listSystemThreads());
            } else if (args[0].equals("gc")) {
                jdp_console.writeOutput(user.listGCThreads());
            } else {
                printHelp(command);
            }
        } else {
            printHelp(command);
        }
    }
