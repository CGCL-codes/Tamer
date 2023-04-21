    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        Channel chan;
        if (argv.length != 2) {
            throw new TclNumArgsException(interp, 1, argv, "channelId");
        }
        chan = TclIO.getChannel(interp, argv[1].toString());
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \"" + argv[1].toString() + "\"");
        }
        interp.setResult(chan.isBlocked(interp));
    }
