    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        Channel chan;
        if ((argv.length < 2) || (((argv.length % 2) == 1) && (argv.length != 3))) {
            throw new TclNumArgsException(interp, 1, argv, "channelId ?optionName? ?value? ?optionName value?...");
        }
        chan = TclIO.getChannel(interp, argv[1].toString());
        if (chan == null) {
            throw new TclException(interp, "can not find channel named \"" + argv[1].toString() + "\"");
        }
        if (argv.length == 2) {
            TclObject list = TclList.newInstance();
            TclList.append(interp, list, TclString.newInstance("-blocking"));
            TclList.append(interp, list, TclBoolean.newInstance(chan.getBlocking()));
            TclList.append(interp, list, TclString.newInstance("-buffering"));
            TclList.append(interp, list, TclString.newInstance(TclIO.getBufferingString(chan.getBuffering())));
            TclList.append(interp, list, TclString.newInstance("-buffersize"));
            TclList.append(interp, list, TclInteger.newInstance(chan.getBufferSize()));
            TclList.append(interp, list, TclString.newInstance("-encoding"));
            String javaEncoding = chan.getEncoding();
            String tclEncoding;
            if (javaEncoding == null) {
                tclEncoding = "binary";
            } else {
                tclEncoding = EncodingCmd.getTclName(javaEncoding);
            }
            TclList.append(interp, list, TclString.newInstance(tclEncoding));
            TclList.append(interp, list, TclString.newInstance("-eofchar"));
            if (chan.isReadOnly()) {
                char eofChar = chan.getInputEofChar();
                TclList.append(interp, list, (eofChar == 0) ? TclString.newInstance("") : TclString.newInstance(eofChar));
            } else if (chan.isWriteOnly()) {
                char eofChar = chan.getOutputEofChar();
                TclList.append(interp, list, (eofChar == 0) ? TclString.newInstance("") : TclString.newInstance(eofChar));
            } else if (chan.isReadWrite()) {
                char inEofChar = chan.getInputEofChar();
                char outEofChar = chan.getOutputEofChar();
                TclObject eofchar_pair = TclList.newInstance();
                TclList.append(interp, eofchar_pair, (inEofChar == 0) ? TclString.newInstance("") : TclString.newInstance(inEofChar));
                TclList.append(interp, eofchar_pair, (outEofChar == 0) ? TclString.newInstance("") : TclString.newInstance(outEofChar));
                TclList.append(interp, list, eofchar_pair);
            } else {
            }
            TclList.append(interp, list, TclString.newInstance("-translation"));
            if (chan.isReadOnly()) {
                TclList.append(interp, list, TclString.newInstance(TclIO.getTranslationString(chan.getInputTranslation())));
            } else if (chan.isWriteOnly()) {
                TclList.append(interp, list, TclString.newInstance(TclIO.getTranslationString(chan.getOutputTranslation())));
            } else if (chan.isReadWrite()) {
                TclObject translation_pair = TclList.newInstance();
                TclList.append(interp, translation_pair, TclString.newInstance(TclIO.getTranslationString(chan.getInputTranslation())));
                TclList.append(interp, translation_pair, TclString.newInstance(TclIO.getTranslationString(chan.getOutputTranslation())));
                TclList.append(interp, list, translation_pair);
            } else {
            }
            interp.setResult(list);
        }
        if (argv.length == 3) {
            int index = TclIndex.get(interp, argv[2], validCmds, "option", 0);
            switch(index) {
                case OPT_BLOCKING:
                    {
                        interp.setResult(chan.getBlocking());
                        break;
                    }
                case OPT_BUFFERING:
                    {
                        interp.setResult(TclIO.getBufferingString(chan.getBuffering()));
                        break;
                    }
                case OPT_BUFFERSIZE:
                    {
                        interp.setResult(chan.getBufferSize());
                        break;
                    }
                case OPT_ENCODING:
                    {
                        String javaEncoding = chan.getEncoding();
                        if (javaEncoding == null) {
                            interp.setResult("binary");
                        } else {
                            interp.setResult(EncodingCmd.getTclName(javaEncoding));
                        }
                        break;
                    }
                case OPT_EOFCHAR:
                    {
                        if (chan.isReadOnly()) {
                            char eofChar = chan.getInputEofChar();
                            interp.setResult((eofChar == 0) ? TclString.newInstance("") : TclString.newInstance(eofChar));
                        } else if (chan.isWriteOnly()) {
                            char eofChar = chan.getOutputEofChar();
                            interp.setResult((eofChar == 0) ? TclString.newInstance("") : TclString.newInstance(eofChar));
                        } else if (chan.isReadWrite()) {
                            char inEofChar = chan.getInputEofChar();
                            char outEofChar = chan.getOutputEofChar();
                            TclObject eofchar_pair = TclList.newInstance();
                            TclList.append(interp, eofchar_pair, (inEofChar == 0) ? TclString.newInstance("") : TclString.newInstance(inEofChar));
                            TclList.append(interp, eofchar_pair, (outEofChar == 0) ? TclString.newInstance("") : TclString.newInstance(outEofChar));
                            interp.setResult(eofchar_pair);
                        } else {
                        }
                        break;
                    }
                case OPT_TRANSLATION:
                    {
                        if (chan.isReadOnly()) {
                            interp.setResult(TclIO.getTranslationString(chan.getInputTranslation()));
                        } else if (chan.isWriteOnly()) {
                            interp.setResult(TclIO.getTranslationString(chan.getOutputTranslation()));
                        } else if (chan.isReadWrite()) {
                            TclObject translation_pair = TclList.newInstance();
                            TclList.append(interp, translation_pair, TclString.newInstance(TclIO.getTranslationString(chan.getInputTranslation())));
                            TclList.append(interp, translation_pair, TclString.newInstance(TclIO.getTranslationString(chan.getOutputTranslation())));
                            interp.setResult(translation_pair);
                        } else {
                        }
                        break;
                    }
                default:
                    {
                        throw new TclRuntimeError("Fconfigure.cmdProc() error: " + "incorrect index returned from TclIndex.get()");
                    }
            }
        }
        for (int i = 3; i < argv.length; i += 2) {
            int index = TclIndex.get(interp, argv[i - 1], validCmds, "option", 0);
            switch(index) {
                case OPT_BLOCKING:
                    {
                        chan.setBlocking(TclBoolean.get(interp, argv[i]));
                        break;
                    }
                case OPT_BUFFERING:
                    {
                        int id = TclIO.getBufferingID(argv[i].toString());
                        if (id == -1) {
                            throw new TclException(interp, "bad value for -buffering: must be " + "one of full, line, or none");
                        }
                        chan.setBuffering(id);
                        break;
                    }
                case OPT_BUFFERSIZE:
                    {
                        chan.setBufferSize(TclInteger.get(interp, argv[i]));
                        break;
                    }
                case OPT_ENCODING:
                    {
                        String tclEncoding = argv[i].toString();
                        if (tclEncoding.equals("") || tclEncoding.equals("binary")) {
                            chan.setEncoding(null);
                        } else {
                            String javaEncoding = EncodingCmd.getJavaName(tclEncoding);
                            if (javaEncoding == null) {
                                throw new TclException(interp, "unknown encoding \"" + tclEncoding + "\"");
                            }
                            if (!EncodingCmd.isSupported(javaEncoding)) {
                                throw new TclException(interp, "unsupported encoding \"" + tclEncoding + "\"");
                            }
                            chan.setEncoding(javaEncoding);
                        }
                        break;
                    }
                case OPT_EOFCHAR:
                    {
                        int length = TclList.getLength(interp, argv[i]);
                        if (length > 2) {
                            throw new TclException(interp, "bad value for -eofchar: " + "should be a list of zero, one, or two elements");
                        }
                        char inputEofChar, outputEofChar;
                        String s;
                        if (length == 0) {
                            inputEofChar = outputEofChar = 0;
                        } else if (length == 1) {
                            s = TclList.index(interp, argv[i], 0).toString();
                            inputEofChar = outputEofChar = s.charAt(0);
                        } else {
                            s = TclList.index(interp, argv[i], 0).toString();
                            inputEofChar = s.charAt(0);
                            s = TclList.index(interp, argv[i], 1).toString();
                            outputEofChar = s.charAt(0);
                        }
                        chan.setInputEofChar(inputEofChar);
                        chan.setOutputEofChar(outputEofChar);
                        break;
                    }
                case OPT_TRANSLATION:
                    {
                        int length = TclList.getLength(interp, argv[i]);
                        if (length < 1 || length > 2) {
                            throw new TclException(interp, "bad value for -translation: " + "must be a one or two element list");
                        }
                        String inputTranslationArg, outputTranslationArg;
                        int inputTranslation, outputTranslation;
                        if (length == 2) {
                            inputTranslationArg = TclList.index(interp, argv[i], 0).toString();
                            inputTranslation = TclIO.getTranslationID(inputTranslationArg);
                            outputTranslationArg = TclList.index(interp, argv[i], 1).toString();
                            outputTranslation = TclIO.getTranslationID(outputTranslationArg);
                        } else {
                            outputTranslationArg = inputTranslationArg = argv[i].toString();
                            outputTranslation = inputTranslation = TclIO.getTranslationID(outputTranslationArg);
                        }
                        if ((inputTranslation == -1) || (outputTranslation == -1)) {
                            throw new TclException(interp, "bad value for -translation: " + "must be one of auto, binary, cr, lf, " + "crlf, or platform");
                        }
                        if (outputTranslation == TclIO.TRANS_AUTO) outputTranslation = TclIO.TRANS_PLATFORM;
                        if (chan.isReadOnly()) {
                            chan.setInputTranslation(inputTranslation);
                            if (inputTranslationArg.equals("binary")) {
                                chan.setEncoding(null);
                            }
                        } else if (chan.isWriteOnly()) {
                            chan.setOutputTranslation(outputTranslation);
                            if (outputTranslationArg.equals("binary")) {
                                chan.setEncoding(null);
                            }
                        } else if (chan.isReadWrite()) {
                            chan.setInputTranslation(inputTranslation);
                            chan.setOutputTranslation(outputTranslation);
                            if (inputTranslationArg.equals("binary") || outputTranslationArg.equals("binary")) {
                                chan.setEncoding(null);
                            }
                        } else {
                        }
                        break;
                    }
                default:
                    {
                        throw new TclRuntimeError("Fconfigure.cmdProc() error: " + "incorrect index returned from TclIndex.get()");
                    }
            }
        }
    }
