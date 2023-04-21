    public void doThread(String command, String[] args) {
        int threadID, threadPointer;
        try {
            switch(args.length) {
                case 0:
                    threadID = user.reg.getContextThreadID();
                    if (threadID != 0) jdp_console.writeOutput("context had been set to thread: " + threadID);
                    threadID = user.reg.registerToTPIndex(user.reg.hardwareTP());
                    jdp_console.writeOutput("setting context to executing thread: " + threadID);
                    user.reg.setContextThreadID(threadID);
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("off")) {
                        user.reg.setContextThreadID(0);
                    } else {
                        threadID = Integer.parseInt(args[0]);
                        user.reg.setContextThreadID(threadID);
                    }
                    break;
                default:
                    printHelp(command);
            }
        } catch (NumberFormatException e) {
            jdp_console.writeOutput("invalid thread ID");
        } catch (Exception e1) {
            jdp_console.writeOutput(e1.getMessage());
        }
    }
