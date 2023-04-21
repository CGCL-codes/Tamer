    public Value doApply(Interpreter f) throws ContinuationException {
        switch(f.vlr.length) {
            case 0:
                switch(id) {
                    case CHARREADY:
                        try {
                            return truth(f.dynenv.getCurrentInReader().ready());
                        } catch (IOException e) {
                            return FALSE;
                        }
                    case FLUSHOUTPUTPORT:
                        try {
                            f.dynenv.getCurrentOutWriter().flush();
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorflushing", f.dynenv.out.toString()), e);
                        }
                        return VOID;
                    case PEEKCHAR:
                        return peekChar(f, charinport(f.dynenv.getCurrentInPort()));
                    case PEEKBYTE:
                        return peekByte(f, bininport(f.dynenv.getCurrentInPort()));
                    case READ:
                        return read(f, charinport(f.dynenv.getCurrentInPort()));
                    case READBYTE:
                        return readByte(f, bininport(f.dynenv.getCurrentInPort()));
                    case READCHAR:
                        return readChar(f, charinport(f.dynenv.getCurrentInPort()));
                    case READCODE:
                        return readCode(f, charinport(f.dynenv.getCurrentInPort()));
                    default:
                        throwArgSizeException();
                }
            case 1:
                switch(id) {
                    case PORTQ:
                        return truth(f.vlr[0] instanceof Port);
                    case INPORTQ:
                        return truth(f.vlr[0] instanceof InputPort);
                    case OUTPORTQ:
                        return truth(f.vlr[0] instanceof OutputPort);
                    case CHARREADY:
                        InputPort inport = charinport(f.vlr[0]);
                        try {
                            return truth(inport.ready());
                        } catch (IOException e) {
                            return FALSE;
                        }
                    case DISPLAY:
                        return displayOrWrite(f, charoutport(f.dynenv.getCurrentOutPort()), f.vlr[0], true);
                    case WRITE:
                        return displayOrWrite(f, charoutport(f.dynenv.getCurrentOutPort()), f.vlr[0], false);
                    case PEEKBYTE:
                        return peekByte(f, bininport(f.vlr[0]));
                    case PEEKCHAR:
                        return peekChar(f, charinport(f.vlr[0]));
                    case READ:
                        SchemeCharacterInputPort cinport = charinport(f.vlr[0]);
                        return read(f, cinport);
                    case READBYTE:
                        SchemeBinaryInputPort binport = bininport(f.vlr[0]);
                        return readByte(f, binport);
                    case READCHAR:
                        cinport = charinport(f.vlr[0]);
                        return readChar(f, cinport);
                    case READCODE:
                        cinport = charinport(f.vlr[0]);
                        return readCode(f, cinport);
                    case OPENAUTOFLUSHSTREAM:
                        System.err.println(warn("autoflushdeprecated"));
                        return new SchemeBinaryOutputPort(new AutoflushOutputStream(binoutstream(f.vlr[0])));
                    case OPENAUTOFLUSHWRITER:
                        System.err.println(warn("autoflushdeprecated"));
                        return new SchemeCharacterOutputPort(new AutoflushWriter(charoutwriter(f.vlr[0])));
                    case OPENCHARINPUTPORT:
                        return new SchemeCharacterInputPort(new PushbackReader(new BufferedReader(f.dynenv.getCharacterSet().newInputStreamReader(bininstream(f.vlr[0])))));
                    case OPENCHAROUTPUTPORT:
                        return new SchemeCharacterOutputPort(new BufferedWriter(f.dynenv.getCharacterSet().newOutputStreamWriter(binoutstream(f.vlr[0]))));
                    case OPENSOURCEINPUTFILE:
                        URL url = url(f.vlr[0]);
                        return openCharInFile(f, url, f.dynenv.characterSet);
                    case OPENINPUTFILE:
                        url = url(f.vlr[0]);
                        return openCharInFile(f, url, f.dynenv.characterSet);
                    case OPENOUTPUTFILE:
                        url = url(f.vlr[0]);
                        return openCharOutFile(f, url, f.dynenv.characterSet, false);
                    case OPENBUFFEREDCHARINPORT:
                        return new SchemeCharacterInputPort(new BufferedReader(charinreader(f.vlr[0])));
                    case OPENBUFFEREDCHAROUTPORT:
                        return new SchemeCharacterOutputPort(new BufferedWriter(charoutwriter(f.vlr[0])));
                    case FLUSHOUTPUTPORT:
                        OutputPort op = outport(f.vlr[0]);
                        try {
                            op.flush();
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorflushing", op.toString()), e);
                        }
                        return VOID;
                    case CLOSEINPUTPORT:
                        InputPort inp = inport(f.vlr[0]);
                        try {
                            if (inp != f.dynenv.in) inp.close();
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorclosing", inp.toString()), e);
                        }
                        return VOID;
                    case CLOSEOUTPUTPORT:
                        OutputPort outp = outport(f.vlr[0]);
                        try {
                            if (outp != f.dynenv.out) outp.close();
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorclosing", outp.toString()), e);
                        }
                        return VOID;
                    case INPORTLOCATION:
                        Reader in = charinreader(f.vlr[0]);
                        if (in instanceof SourceReader) {
                            SourceReader sinp = (SourceReader) in;
                            return sourceAnnotations(sinp.sourceFile, sinp.line, sinp.column, f.dynenv.sourceAnnotations);
                        } else return FALSE;
                    case LOAD:
                        load(f, url(f.vlr[0]), false);
                        return VOID;
                    case LOADEXPANDED:
                        load(f, url(f.vlr[0]), true);
                        return VOID;
                    case WRITECHAR:
                        try {
                            f.dynenv.getCurrentOutWriter().write(character(f.vlr[0]));
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorwriting", f.dynenv.out.toString(), e.getMessage()), e);
                        }
                        return VOID;
                    case WRITEBYTE:
                        try {
                            binoutstream(f.dynenv.getCurrentOutPort()).write(num(f.vlr[0]).indexValue());
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorwriting", f.dynenv.out.toString(), e.getMessage()), e);
                        }
                        return VOID;
                    case FILEEXISTSQ:
                        try {
                            url(f.vlr[0]).openConnection().getInputStream().close();
                            return TRUE;
                        } catch (IOException e) {
                            return FALSE;
                        }
                    case FINDRESOURCE:
                        url = Util.currentClassLoader().getResource(string(f.vlr[0]));
                        if (url == null) return FALSE; else return new SchemeString(url.toString());
                    case FINDRESOURCES:
                        java.util.Enumeration e;
                        try {
                            e = Util.currentClassLoader().getResources(string(f.vlr[0]));
                        } catch (IOException ex) {
                            return EMPTYLIST;
                        }
                        if (!e.hasMoreElements()) return EMPTYLIST;
                        Pair pa = new Pair();
                        while (true) {
                            pa.setCar(new SchemeString((String) e.nextElement()));
                            if (!e.hasMoreElements()) break;
                            pa.setCdr(new Pair());
                            pa = (Pair) pa.cdr();
                        }
                        return pa;
                    case ABSPATHQ:
                        String f1 = string(f.vlr[0]);
                        if (f1.startsWith("file:")) f1 = f1.substring(5);
                        File fn = new File(f1);
                        return truth(fn.isAbsolute());
                    case NORMALIZEURL:
                        URL u = urlClean(url(f.vlr[0]));
                        return new SchemeString(u.toString());
                    default:
                        throwArgSizeException();
                }
            case 2:
                switch(id) {
                    case WRITECHAR:
                        Writer port = charoutwriter(f.vlr[1]);
                        try {
                            port.write(character(f.vlr[0]));
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorwriting", port.toString(), e.getMessage()), e);
                        }
                        return VOID;
                    case WRITEBYTE:
                        OutputStream bport = binoutstream(f.vlr[1]);
                        try {
                            bport.write(num(f.vlr[0]).indexValue());
                        } catch (IOException e) {
                            throwIOException(f, liMessage(IOB, "errorwriting", bport.toString(), e.getMessage()), e);
                        }
                        return VOID;
                    case DISPLAY:
                        return displayOrWrite(f, charoutport(f.vlr[1]), f.vlr[0], true);
                    case WRITE:
                        return displayOrWrite(f, charoutport(f.vlr[1]), f.vlr[0], false);
                    case OPENCHARINPUTPORT:
                        try {
                            return new SchemeCharacterInputPort(new PushbackReader(new BufferedReader(Charset.forName(string(f.vlr[1])).newInputStreamReader(bininstream(f.vlr[0])))));
                        } catch (UnsupportedEncodingException use) {
                            throwIOException(f, liMessage(IOB, "unsupencoding", string(f.vlr[1])), new IOException(use.getMessage()));
                        }
                    case OPENCHAROUTPUTPORT:
                        try {
                            return new SchemeCharacterOutputPort(new BufferedWriter(Charset.forName(string(f.vlr[1])).newOutputStreamWriter(binoutstream(f.vlr[0]))));
                        } catch (UnsupportedEncodingException use) {
                            throwIOException(f, liMessage(IOB, "unsupencoding", string(f.vlr[1])), new IOException(use.getMessage()));
                        }
                    case OPENINPUTFILE:
                        URL url = url(f.vlr[0]);
                        return openCharInFile(f, url, Util.charsetFromString(string(f.vlr[1])));
                    case OPENOUTPUTFILE:
                        url = url(f.vlr[0]);
                        boolean aflush = false;
                        Charset encoding = f.dynenv.characterSet;
                        if (f.vlr[1] instanceof SchemeString) encoding = Util.charsetFromString(string(f.vlr[1])); else aflush = truth(f.vlr[1]);
                        return openCharOutFile(f, url, encoding, aflush);
                    case OPENBUFFEREDCHARINPORT:
                        return new SchemeCharacterInputPort(new BufferedReader(charinreader(f.vlr[0]), num(f.vlr[1]).indexValue()));
                    case OPENBUFFEREDCHAROUTPORT:
                        return new SchemeCharacterOutputPort(new BufferedWriter(charoutwriter(f.vlr[0]), num(f.vlr[1]).indexValue()));
                    case NORMALIZEURL:
                        return new SchemeString(urlClean(url(f.vlr[0], f.vlr[1])).toString());
                    default:
                        throwArgSizeException();
                }
            case 3:
                switch(id) {
                    case READSTRING:
                        try {
                            int charsRead = str(f.vlr[0]).readFromReader(f.dynenv.getCurrentInReader(), num(f.vlr[1]).intValue(), num(f.vlr[2]).intValue());
                            if (charsRead < 0) return EOF; else return Quantity.valueOf(charsRead);
                        } catch (IOException e) {
                            throwIOException(f, e.getMessage(), e);
                        }
                        return VOID;
                    case WRITESTRING:
                        try {
                            str(f.vlr[0]).writeToWriter(f.dynenv.getCurrentOutWriter(), num(f.vlr[1]).intValue(), num(f.vlr[2]).intValue());
                        } catch (IOException e) {
                            throwIOException(f, e.getMessage(), e);
                        }
                        return VOID;
                    case OPENOUTPUTFILE:
                        URL url = url(f.vlr[0]);
                        return openCharOutFile(f, url, Util.charsetFromString(string(f.vlr[1])), truth(f.vlr[2]));
                    default:
                        throwArgSizeException();
                }
            case 4:
                switch(id) {
                    case READSTRING:
                        try {
                            int charsRead = str(f.vlr[0]).readFromReader(charinreader(f.vlr[3]), num(f.vlr[1]).intValue(), num(f.vlr[2]).intValue());
                            if (charsRead < 0) return EOF; else return Quantity.valueOf(charsRead);
                        } catch (IOException e) {
                            throwIOException(f, e.getMessage(), e);
                        }
                        return VOID;
                    case WRITESTRING:
                        try {
                            str(f.vlr[0]).writeToWriter(charoutwriter(f.vlr[3]), num(f.vlr[1]).intValue(), num(f.vlr[2]).intValue());
                        } catch (IOException e) {
                            throwIOException(f, e.getMessage(), e);
                        }
                        return VOID;
                    default:
                        throwArgSizeException();
                }
            default:
                throwArgSizeException();
        }
        return VOID;
    }
