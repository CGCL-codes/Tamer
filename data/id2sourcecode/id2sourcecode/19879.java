    public void doThread(String command, String[] args) {
        int threadID, threadPointer;
        try {
            switch(args.length) {
                case 0:
                    threadID = user.reg.threadPointerToIndex(user.reg.hardwareTP());
                    jdp_console.writeOutput("context of executing thread: " + threadID);
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
