    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        Channel chan;
        if (argv.length == 2) {
            if (!argv[1].toString().equals("open")) {
                throw new TclNumArgsException(interp, 1, argv, "only option open can have two args");
            }
        } else if (argv.length != 3) {
            throw new TclNumArgsException(interp, 1, argv, "command channelId");
        }
        if (argv.length == 2) {
            HashMap chanTable = TclIO.getInterpChanTable(interp);
            TclObject list = TclList.newInstance();
            for (Iterator iter = chanTable.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                chan = (Channel) entry.getValue();
                TclList.append(interp, list, TclString.newInstance(chan.getChanName()));
            }
            interp.setResult(list);
            return;
        }
        chan = TclIO.getChannel(interp, argv[2].toString());
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \"" + argv[2].toString() + "\"");
        }
        int index = TclIndex.get(interp, argv[1], validCmds, "option", 0);
        switch(index) {
            case OPT_INFO:
                {
                    TclObject list = TclList.newInstance();
                    TclList.append(interp, list, argv[2]);
                    TclList.append(interp, list, TclString.newInstance(chan.getChanType()));
                    if (chan.isReadOnly() || chan.isReadWrite()) {
                        TclList.append(interp, list, TclString.newInstance("read"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance(""));
                    }
                    if (chan.isWriteOnly() || chan.isReadWrite()) {
                        TclList.append(interp, list, TclString.newInstance("write"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance(""));
                    }
                    if (chan.getBlocking()) {
                        TclList.append(interp, list, TclString.newInstance("blocking"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance("nonblocking"));
                    }
                    if (chan.getBuffering() == TclIO.BUFF_FULL) {
                        TclList.append(interp, list, TclString.newInstance("full"));
                    } else if (chan.getBuffering() == TclIO.BUFF_LINE) {
                        TclList.append(interp, list, TclString.newInstance("line"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance("none"));
                    }
                    if (chan.isBgFlushScheduled()) {
                        TclList.append(interp, list, TclString.newInstance("async_flush"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance(""));
                    }
                    if (chan.eof()) {
                        TclList.append(interp, list, TclString.newInstance("eof"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance(""));
                    }
                    if (chan.isBlocked(interp)) {
                        TclList.append(interp, list, TclString.newInstance("blocked"));
                    } else {
                        TclList.append(interp, list, TclString.newInstance("unblocked"));
                    }
                    int translation = chan.getInputTranslation();
                    if (translation == TclIO.TRANS_AUTO) {
                        TclList.append(interp, list, TclString.newInstance("auto"));
                        TclList.append(interp, list, TclString.newInstance(chan.inputSawCR() ? "queued_cr" : ""));
                    } else if (translation == TclIO.TRANS_LF) {
                        TclList.append(interp, list, TclString.newInstance("lf"));
                        TclList.append(interp, list, TclString.newInstance(""));
                    } else if (translation == TclIO.TRANS_CR) {
                        TclList.append(interp, list, TclString.newInstance("cr"));
                        TclList.append(interp, list, TclString.newInstance(""));
                    } else if (translation == TclIO.TRANS_CRLF) {
                        TclList.append(interp, list, TclString.newInstance("crlf"));
                        TclList.append(interp, list, TclString.newInstance(""));
                    }
                    translation = chan.getOutputTranslation();
                    if (translation == TclIO.TRANS_AUTO) {
                        TclList.append(interp, list, TclString.newInstance("auto"));
                    } else if (translation == TclIO.TRANS_LF) {
                        TclList.append(interp, list, TclString.newInstance("lf"));
                    } else if (translation == TclIO.TRANS_CR) {
                        TclList.append(interp, list, TclString.newInstance("cr"));
                    } else if (translation == TclIO.TRANS_CRLF) {
                        TclList.append(interp, list, TclString.newInstance("crlf"));
                    }
                    TclList.append(interp, list, TclInteger.newInstance(chan.getNumBufferedInputBytes()));
                    TclList.append(interp, list, TclInteger.newInstance(chan.getNumBufferedOutputBytes()));
                    try {
                        TclList.append(interp, list, TclInteger.newInstance((int) chan.tell()));
                    } catch (IOException e) {
                        throw new TclException(interp, e.toString());
                    }
                    TclList.append(interp, list, TclInteger.newInstance(chan.getRefCount()));
                    interp.setResult(list);
                    break;
                }
            case OPT_INPUTBUFFERED:
                {
                    interp.setResult(chan.getNumBufferedInputBytes());
                    break;
                }
            case OPT_NAME:
                {
                    interp.setResult(chan.getChanName());
                    break;
                }
            case OPT_OUTPUTBUFFERED:
                {
                    interp.setResult(chan.getNumBufferedOutputBytes());
                    break;
                }
            case OPT_QUEUEDCR:
                {
                    interp.setResult(chan.inputSawCR());
                    break;
                }
            case OPT_REFCOUNT:
                {
                    interp.setResult(chan.getRefCount());
                    break;
                }
            case OPT_TYPE:
                {
                    interp.setResult(chan.getChanType());
                    break;
                }
            default:
                {
                    throw new TclRuntimeError("TestChannel.cmdProc() error: " + "incorrect index returned from TclIndex.get()");
                }
        }
    }
