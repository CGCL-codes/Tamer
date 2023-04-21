    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        Channel chan;
        String channelId;
        String arg;
        int i = 1;
        boolean newline = true;
        if ((argv.length >= 2) && (argv[1].toString().equals("-nonewline"))) {
            newline = false;
            i++;
        }
        if ((i < argv.length - 3) || (i >= argv.length)) {
            throw new TclNumArgsException(interp, 1, argv, "?-nonewline? ?channelId? string");
        }
        if (i == (argv.length - 3)) {
            arg = argv[i + 2].toString();
            if (!arg.equals("nonewline")) {
                throw new TclException(interp, "bad argument \"" + arg + "\": should be \"nonewline\"");
            }
            newline = false;
        }
        if (i == (argv.length - 1)) {
            channelId = "stdout";
        } else {
            channelId = argv[i].toString();
            i++;
        }
        if (i != (argv.length - 1)) {
            throw new TclNumArgsException(interp, 1, argv, "?-nonewline? ?channelId? string");
        }
        chan = TclIO.getChannel(interp, channelId);
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \"" + channelId + "\"");
        }
        try {
            if (newline) {
                chan.write(interp, argv[i]);
                chan.write(interp, "\n");
            } else {
                chan.write(interp, argv[i]);
            }
        } catch (IOException e) {
            throw new TclRuntimeError("PutsCmd.cmdProc() Error: IOException when putting " + chan.getChanName());
        }
    }
