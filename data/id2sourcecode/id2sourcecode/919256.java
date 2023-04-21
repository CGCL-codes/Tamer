    public void doPrintCommand(String command, String[] args) {
        int addr, frame;
        String varname;
        if (args.length == 0) {
            jdp_console.writeOutput(user.bmap.localVariableToString(0, null));
            return;
        }
        String arg1;
        if (args[0].startsWith("(")) {
            int rparen = args[0].indexOf(')');
            if (rparen == -1) {
                jdp_console.writeOutput("missing parenthesis for class name: " + args[0]);
                return;
            }
            if (args.length == 1) {
                if (rparen != (args[0].length() - 1)) {
                    arg1 = args[0].substring(rparen + 1);
                } else {
                    jdp_console.writeOutput("Please specify an address to be cast e.g. print (VM_Thread) 0x4169536c");
                    return;
                }
            } else arg1 = args[1];
            try {
                addr = parseHex32(arg1);
                String classname = args[0].substring(1, rparen);
                try {
                    jdp_console.writeOutput(classname + " = " + user.bmap.classToString(classname, addr, false));
                } catch (memoryException e) {
                    jdp_console.writeOutput("(" + e.getMessage() + ")");
                }
            } catch (NumberFormatException e) {
                jdp_console.writeOutput("bad address for casting: " + args[1]);
            }
            return;
        }
        frame = CommandLine.localParseFrame(args[0]);
        varname = CommandLine.localParseName(args[0]);
        if (frame != -1) {
            if (varname == null) {
                jdp_console.writeOutput(user.bmap.localVariableToString(frame, null));
            } else {
                if (varname.equals("this") || varname.startsWith("this.")) jdp_console.writeOutput(args[0] + " = " + user.bmap.currentClassToString(frame, varname)); else jdp_console.writeOutput(user.bmap.localVariableToString(frame, varname));
            }
            return;
        }
        if (varname.equals("this") || varname.startsWith("this.")) jdp_console.writeOutput(args[0] + " = " + user.bmap.currentClassToString(0, varname)); else jdp_console.writeOutput(user.bmap.localVariableToString(0, varname));
        return;
    }
