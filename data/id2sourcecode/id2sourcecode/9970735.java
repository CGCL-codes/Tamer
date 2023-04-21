    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        boolean writeToVar = false;
        String varName = "";
        Channel chan;
        int lineLen;
        TclObject line;
        if ((argv.length < 2) || (argv.length > 3)) {
            throw new TclNumArgsException(interp, 1, argv, "channelId ?varName?");
        }
        if (argv.length == 3) {
            writeToVar = true;
            varName = argv[2].toString();
        }
        chan = TclIO.getChannel(interp, argv[1].toString());
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \"" + argv[1].toString() + "\"");
        }
        try {
            line = TclString.newInstance(new StringBuffer(64));
            lineLen = chan.read(interp, line, TclIO.READ_LINE, 0);
            if (lineLen < 0) {
                if (!chan.eof() && !chan.isBlocked(interp)) {
                    throw new TclPosixException(interp, TclPosixException.EIO, true, "error reading \"" + argv[1].toString() + "\"");
                }
                lineLen = -1;
            }
            if (writeToVar) {
                interp.setVar(varName, line, 0);
                interp.setResult(lineLen);
            } else {
                interp.setResult(line);
            }
        } catch (IOException e) {
            throw new TclRuntimeError("GetsCmd.cmdProc() Error: IOException when getting " + chan.getChanName() + ": " + e.getMessage());
        }
    }
