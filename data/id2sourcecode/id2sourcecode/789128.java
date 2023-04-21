    private boolean jdpCommand(String command, String[] args) {
        runstat = true;
        int addr, count;
        if (user == null) {
            if (command.equals("run")) {
                switch(args.length) {
                    case 0:
                        restart(saved_args);
                        break;
                    default:
                        restart(args);
                }
            } else if (command.equals("help") || command.equals("h") || command.equals("?")) {
                if (args.length == 0) printHelp(""); else printHelp(args[0]);
            } else {
                jdp_console.writeOutput("No program running, enter:  run ... ");
            }
            return false;
        }
        if (command.equals("step") || command.equals("s")) {
            if (args.length != 0) jdp_console.writeOutput("Sorry, step does not take any arguments at this time. Ignoring arguments.");
            boolean skip_prolog = false;
            printMode = PRINTASSEMBLY;
            runstat = user.pstep(0, printMode, skip_prolog);
            if (runstat == true) refreshEnvironment();
        } else if (command.equals("stepbr") || command.equals("sbr")) {
            if (Platform.stepbrImplemented == 1) {
                if (args.length != 0) jdp_console.writeOutput("Sorry, stepbr does not take any arguments at this time. Ignoring arguments.");
                printMode = PRINTASSEMBLY;
                runstat = user.pstepOverBranch(0);
                if (runstat == true) refreshEnvironment();
            } else {
                jdp_console.writeOutput("Sorry, step instruction over call is not supported yet on this platform");
            }
        } else if (command.equals("stepline") || command.equals("sl")) {
            if (args.length != 0) jdp_console.writeOutput("Sorry, stepline does not take any arguments at this time. Ignoring arguments.");
            printMode = PRINTASSEMBLY;
            runstat = user.pstepLine(0, printMode);
            if (runstat == true) refreshEnvironment();
        } else if (command.equals("steplineover") || command.equals("slo")) {
            if (args.length != 0) jdp_console.writeOutput("Sorry, steplineover does not take any arguments at this time. Ignoring arguments.");
            printMode = PRINTSOURCE;
            runstat = user.pstepLineOverMethod(0);
            if (runstat == true) refreshEnvironment();
        } else if (command.equals("run")) {
            jdp_console.writeOutput("Debuggee is running, kill before restarting");
        } else if (command.equals("kill") || command.equals("k")) {
            switch(debuggerEnvironment) {
                case EXTERNALCREATE:
                    runstat = false;
                    break;
                case EXTERNALATTACH:
                    jdp_console.writeOutput("Cannot kill attached process, type quit to detach debugger");
                    break;
                case INTERNAL:
                    jdp_console.writeOutput("Debugger running inside JVM, type quit to exit debugger");
            }
        } else if (command.equals("cont") || command.equals("c")) {
            if (debuggerEnvironment == EXTERNALATTACH && !user.bpset.anyBreakpointExist()) {
                jdp_console.writeOutput("no breakpoint currently set, detaching process");
                return true;
            } else {
                if (args.length != 0) jdp_console.writeOutput("This command does not take any arguments. Ignoring arguments.");
                runstat = user.pcontinue(0, printMode, true);
                if (runstat == true) refreshEnvironment();
            }
        } else if (command.equals("cthread") || command.equals("ct")) {
            if (Platform.cthreadImplemented == 1) {
                if (args.length != 0) jdp_console.writeOutput("This command does not take any arguments. Ignoring arguments.");
                runstat = user.pcontinue(0, printMode, false);
                if (runstat == true) refreshEnvironment();
            } else {
                jdp_console.writeOutput("Sorry, continue thread is not supported yet on this platform");
            }
        } else if (command.equals("creturn") || command.equals("cr")) {
            if (args.length != 0) jdp_console.writeOutput("This command does not take any arguments. Ignoring arguments.");
            runstat = user.pcontinueToReturn(0, printMode);
            if (runstat == true) refreshEnvironment();
        } else if (command.equals("thread") || command.equals("th")) {
            doThread(command, args);
        } else if (command.equals("reg") || command.equals("r")) {
            doRegisterRead(command, args);
        } else if (command.equals("wreg") || command.equals("wr")) {
            doRegisterWrite(command, args);
        } else if (command.equals("memraw") || command.equals("mraw")) {
            doMemoryReadRaw(command, args);
        } else if (command.equals("mem") || command.equals("m")) {
            doMemoryRead(command, args);
        } else if (command.equals("wmem") || command.equals("wm")) {
            doMemoryWrite(command, args);
        } else if (command.equals("print") || command.equals("p")) {
            doPrintCommand(command, args);
        } else if (command.equals("printclass") || command.equals("pc")) {
            doPrintClassCommand(command, args);
        } else if (command.equals("getclass")) {
            doGetClassCommand(command, args);
        } else if (command.equals("getinstance")) {
            doGetInstanceCommand(command, args);
        } else if (command.equals("getarray")) {
            doGetArrayCommand(command, args);
        } else if (command.equals("getcl")) {
            doGetClassAndLine(command, args);
        } else if (command.equals("getcia")) {
            doGetCurrentInstrAddr(command, args);
        } else if (command.equals("getframes")) {
            doGetFrames(command, args);
        } else if (command.equals("getlocals")) {
            doGetLocals(command, args);
        } else if (command.equals("listb") || command.equals("lb")) {
            jdp_console.writeOutput("(this command has been removed because the Opt compiler does not generate the bytecode map)");
        } else if (command.equals("listi") || command.equals("li")) {
            doListInstruction(command, args);
        } else if (command.equals("listt") || command.equals("lt")) {
            doListThread(command, args);
        } else if (command.equals("break") || command.equals("b")) {
            doSetBreakpoint(command, args);
        } else if (command.equals("clearbreak") || command.equals("cb")) {
            doClearBreakpoint(command, args);
        } else if (command.equals("stack") || command.equals("f")) {
            doCurrentFrame(command, args);
        } else if (command.equals("where") || command.equals("w")) {
            doShortFrame(command, args);
        } else if (command.equals("whereframe") || command.equals("wf")) {
            doFullFrame(command, args);
        } else if (command.equals("preference") || command.equals("pref")) {
            doSetPreference(command, args);
        } else if (command.equals("preference") || command.equals("x2d")) {
            doConvertHexToInt(command, args);
        } else if (command.equals("preference") || command.equals("d2x")) {
            doConvertIntToHex(command, args);
        } else if (command.equals("test")) {
            doTest(args);
        } else if (command.equals("test1")) {
            doTest1(args);
        } else if (command.equals("count")) {
            doThreadCount(0);
        } else if (command.equals("zerocount")) {
            doThreadCount(1);
        } else if (command.equals("readmem")) {
            if (args.length != 0) {
                try {
                    addr = parseHex32(args[0]);
                    int mydata = user.mem.read(addr);
                    jdp_console.writeOutput("true memory = x" + Integer.toHexString(mydata));
                } catch (NumberFormatException e) {
                    jdp_console.writeOutput("bad address: " + args[0]);
                }
            }
        } else if (command.equals("verbose") || command.equals("v")) {
            if (args.length != 0) jdp_console.writeOutput("This command does not take any arguments. Ignoring arguments.");
            if (user.verbose) {
                jdp_console.writeOutput("Verbose now OFF");
                user.verbose = false;
            } else {
                jdp_console.writeOutput("Verbose now ON");
                user.verbose = true;
            }
        } else if (command.equals("help") || command.equals("h") || command.equals("?")) {
            if (args.length == 0) printHelp(""); else printHelp(args[0]);
        } else if (macro.exists(command + ".jdp")) {
            macro.load(command + ".jdp");
        } else {
            jdp_console.writeOutput("Sorry, you've specified an unknown command. Please use help to see the list of known commands");
        }
        return false;
    }
