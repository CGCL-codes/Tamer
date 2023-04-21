    public void runCommand(String commands) throws Exception {
        System.out.println("> " + commands + '\n');
        System.out.flush();
        String[] commandArgs = Utils.splitOptions(commands);
        if (commandArgs.length == 0) {
            return;
        }
        if (commandArgs[0].equals("java")) {
            commandArgs[0] = "";
            try {
                if (commandArgs.length == 1) {
                    throw new Exception("No class name given");
                }
                String className = commandArgs[1];
                commandArgs[1] = "";
                if (m_RunThread != null) {
                    throw new Exception("An object is already running, use \"break\"" + " to interrupt it.");
                }
                Class theClass = Class.forName(className);
                Vector argv = new Vector();
                for (int i = 2; i < commandArgs.length; i++) argv.add(commandArgs[i]);
                m_RunThread = new ClassRunner(theClass, (String[]) argv.toArray(new String[argv.size()]));
                m_RunThread.setPriority(Thread.MIN_PRIORITY);
                m_RunThread.start();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else if (commandArgs[0].equals("capabilities")) {
            try {
                Object obj = Class.forName(commandArgs[1]).newInstance();
                if (obj instanceof CapabilitiesHandler) {
                    if (obj instanceof OptionHandler) {
                        Vector<String> args = new Vector<String>();
                        for (int i = 2; i < commandArgs.length; i++) args.add(commandArgs[i]);
                        ((OptionHandler) obj).setOptions(args.toArray(new String[args.size()]));
                    }
                    Capabilities caps = ((CapabilitiesHandler) obj).getCapabilities();
                    System.out.println(caps.toString().replace("[", "\n").replace("]", "\n"));
                } else {
                    System.out.println("'" + commandArgs[1] + "' is not a " + CapabilitiesHandler.class.getName() + "!");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else if (commandArgs[0].equals("cls")) {
            m_OutputArea.setText("");
        } else if (commandArgs[0].equals("history")) {
            System.out.println("Command history:");
            for (int i = 0; i < m_CommandHistory.size(); i++) System.out.println(m_CommandHistory.get(i));
            System.out.println();
        } else if (commandArgs[0].equals("break")) {
            if (m_RunThread == null) {
                System.err.println("Nothing is currently running.");
            } else {
                System.out.println("[Interrupt...]");
                m_RunThread.interrupt();
            }
        } else if (commandArgs[0].equals("kill")) {
            if (m_RunThread == null) {
                System.err.println("Nothing is currently running.");
            } else {
                System.out.println("[Kill...]");
                m_RunThread.stop();
                m_RunThread = null;
            }
        } else if (commandArgs[0].equals("exit")) {
            Container parent = getParent();
            Container frame = null;
            boolean finished = false;
            while (!finished) {
                if ((parent instanceof JFrame) || (parent instanceof Frame) || (parent instanceof JInternalFrame)) {
                    frame = parent;
                    finished = true;
                }
                if (!finished) {
                    parent = parent.getParent();
                    finished = (parent == null);
                }
            }
            if (frame != null) {
                if (frame instanceof JInternalFrame) ((JInternalFrame) frame).doDefaultCloseAction(); else ((Window) frame).dispatchEvent(new WindowEvent((Window) frame, WindowEvent.WINDOW_CLOSING));
            }
        } else {
            boolean help = ((commandArgs.length > 1) && commandArgs[0].equals("help"));
            if (help && commandArgs[1].equals("java")) {
                System.out.println("java <classname> <args>\n\n" + "Starts the main method of <classname> with " + "the supplied command line arguments (if any).\n" + "The command is started in a separate thread, " + "and may be interrupted with the \"break\"\n" + "command (friendly), or killed with the \"kill\" " + "command (unfriendly).\n" + "Redirecting can be done with '>' followed by the " + "file to write to, e.g.:\n" + "  java some.Class > ." + File.separator + "some.txt");
            } else if (help && commandArgs[1].equals("break")) {
                System.out.println("break\n\n" + "Attempts to nicely interrupt the running job, " + "if any. If this doesn't respond in an\n" + "acceptable time, use \"kill\".\n");
            } else if (help && commandArgs[1].equals("kill")) {
                System.out.println("kill\n\n" + "Kills the running job, if any. You should only " + "use this if the job doesn't respond to\n" + "\"break\".\n");
            } else if (help && commandArgs[1].equals("capabilities")) {
                System.out.println("capabilities <classname> <args>\n\n" + "Lists the capabilities of the specified class.\n" + "If the class is a " + OptionHandler.class.getName() + " then\n" + "trailing options after the classname will be\n" + "set as well.\n");
            } else if (help && commandArgs[1].equals("cls")) {
                System.out.println("cls\n\n" + "Clears the output area.\n");
            } else if (help && commandArgs[1].equals("history")) {
                System.out.println("history\n\n" + "Prints all issued commands.\n");
            } else if (help && commandArgs[1].equals("exit")) {
                System.out.println("exit\n\n" + "Exits the SimpleCLI program.\n");
            } else {
                System.out.println("Command must be one of:\n" + "\tjava <classname> <args> [ > file]\n" + "\tbreak\n" + "\tkill\n" + "\tcapabilities <classname> <args>\n" + "\tcls\n" + "\thistory\n" + "\texit\n" + "\thelp <command>\n");
            }
        }
    }
