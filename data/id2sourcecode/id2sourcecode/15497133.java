    private final synchronized void processEngineOutput(UCIEngine uci, String s) {
        if (Thread.currentThread().isInterrupted()) return;
        if (s == null) {
            shutdownEngine();
            return;
        }
        if (s.length() == 0) return;
        switch(engineState.state) {
            case READ_OPTIONS:
                {
                    if (readUCIOption(uci, s)) {
                        uci.initOptions(egtbOptions);
                        uci.writeLineToEngine("ucinewgame");
                        uci.writeLineToEngine("isready");
                        engineState.setState(MainState.WAIT_READY);
                    }
                    break;
                }
            case WAIT_READY:
                {
                    if ("readyok".equals(s)) {
                        engineState.setState(MainState.IDLE);
                        handleIdleState();
                    }
                    break;
                }
            case SEARCH:
            case PONDER:
            case ANALYZE:
                {
                    String[] tokens = tokenize(s);
                    if (tokens[0].equals("info")) {
                        parseInfoCmd(tokens);
                    } else if (tokens[0].equals("bestmove")) {
                        String bestMove = tokens[1];
                        String nextPonderMoveStr = "";
                        if ((tokens.length >= 4) && (tokens[2].equals("ponder"))) nextPonderMoveStr = tokens[3];
                        Move nextPonderMove = TextIO.UCIstringToMove(nextPonderMoveStr);
                        if (engineState.state == MainState.SEARCH) reportMove(bestMove, nextPonderMove);
                        engineState.setState(MainState.IDLE);
                        searchRequest = null;
                        handleIdleState();
                    }
                    break;
                }
            case STOP_SEARCH:
                {
                    String[] tokens = tokenize(s);
                    if (tokens[0].equals("bestmove")) {
                        uci.writeLineToEngine("isready");
                        engineState.setState(MainState.WAIT_READY);
                    }
                    break;
                }
            default:
        }
    }
