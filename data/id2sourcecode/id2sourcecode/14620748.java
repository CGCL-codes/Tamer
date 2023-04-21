    boolean readSyncPoint(ArrayList<LongValue> values, boolean print) {
        int token;
        boolean eof = false;
        do {
            try {
                token = _tok.nextToken();
                switch(token) {
                    case StreamTokenizer.TT_NUMBER:
                        long thread = (long) _tok.nval;
                        token = _tok.nextToken();
                        switch(token) {
                            case StreamTokenizer.TT_NUMBER:
                                long code = (long) _tok.nval;
                                if ((code < 0) || (code > SyncPointBuffer.SP.LAST_VALID_CODE.intValue())) {
                                    throw new Error("Error reading trace file at line " + _tok.lineno() + ": unexpected nval " + code);
                                }
                                token = _tok.nextToken();
                                switch(token) {
                                    case StreamTokenizer.TT_EOL:
                                    case StreamTokenizer.TT_EOF:
                                        if (print) {
                                            _writer.println(thread + " " + code);
                                            _writer.flush();
                                        }
                                        if (values != null) {
                                            LongValue tagValue = _vm.mirrorOf(code);
                                            LongValue threadValue = _vm.mirrorOf(thread);
                                            values.add(tagValue);
                                            values.add(threadValue);
                                        }
                                        ++_numTotalSyncPointsRead;
                                        return true;
                                    default:
                                        throw new Error("Error reading trace file: unexpected token " + _tok);
                                }
                            default:
                                throw new Error("Error reading trace file: unexpected token " + _tok);
                        }
                    case StreamTokenizer.TT_EOL:
                        break;
                    case StreamTokenizer.TT_EOF:
                        eof = true;
                        break;
                    default:
                        throw new Error("Error reading trace file: unexpected token " + _tok);
                }
            } catch (IOException e) {
                throw new Error("Error reading trace file: " + _tok + ", " + e.toString());
            }
        } while (!eof);
        return false;
    }
