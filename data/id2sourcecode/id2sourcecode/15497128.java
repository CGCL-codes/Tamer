    private final void handleIdleState() {
        SearchRequest sr = searchRequest;
        if (sr == null) return;
        if ((uciEngine == null) || !engineState.engine.equals(sr.engine)) {
            shutdownEngine();
            startEngine();
            return;
        }
        if (newGame) {
            uciEngine.writeLineToEngine("ucinewgame");
            uciEngine.writeLineToEngine("isready");
            engineState.setState(MainState.WAIT_READY);
            newGame = false;
            return;
        }
        boolean isSearch = sr.isSearch;
        boolean isAnalyze = sr.isAnalyze;
        if (!isSearch && !isAnalyze) {
            searchRequest = null;
            return;
        }
        engineState.searchId = searchRequest.searchId;
        if (isSearch) {
            long now = System.currentTimeMillis();
            int delay = (int) (now - searchRequest.startTime);
            boolean wtm = searchRequest.currPos.whiteMove ^ (searchRequest.ponderMove != null);
            if (wtm) searchRequest.wTime = Math.max(1, searchRequest.wTime - delay); else searchRequest.bTime = Math.max(1, searchRequest.bTime - delay);
        }
        clearInfo();
        uciEngine.setStrength(searchRequest.strength);
        if (maxPV > 1) {
            int num = Math.min(maxPV, searchRequest.numPV);
            uciEngine.setOption("MultiPV", num);
        }
        if (isSearch) {
            StringBuilder posStr = new StringBuilder();
            posStr.append("position fen ");
            posStr.append(TextIO.toFEN(sr.prevPos));
            int nMoves = sr.mList.size();
            if (nMoves > 0) {
                posStr.append(" moves");
                for (int i = 0; i < nMoves; i++) {
                    posStr.append(" ");
                    posStr.append(TextIO.moveToUCIString(sr.mList.get(i)));
                }
            }
            uciEngine.setOption("Ponder", sr.ponderEnabled);
            uciEngine.setOption("UCI_AnalyseMode", false);
            uciEngine.setOption("Threads", sr.engineThreads > 0 ? sr.engineThreads : numCPUs);
            uciEngine.writeLineToEngine(posStr.toString());
            if (sr.wTime < 1) sr.wTime = 1;
            if (sr.bTime < 1) sr.bTime = 1;
            StringBuilder goStr = new StringBuilder(96);
            goStr.append(String.format("go wtime %d btime %d", sr.wTime, sr.bTime));
            if (sr.inc > 0) goStr.append(String.format(" winc %d binc %d", sr.inc, sr.inc));
            if (sr.movesToGo > 0) goStr.append(String.format(" movestogo %d", sr.movesToGo));
            if (sr.ponderMove != null) goStr.append(" ponder");
            if (sr.searchMoves != null) {
                goStr.append(" searchmoves");
                for (Move m : sr.searchMoves) {
                    goStr.append(' ');
                    goStr.append(TextIO.moveToUCIString(m));
                }
            }
            uciEngine.writeLineToEngine(goStr.toString());
            engineState.setState((sr.ponderMove == null) ? MainState.SEARCH : MainState.PONDER);
        } else {
            StringBuilder posStr = new StringBuilder();
            posStr.append("position fen ");
            posStr.append(TextIO.toFEN(sr.prevPos));
            int nMoves = sr.mList.size();
            if (nMoves > 0) {
                posStr.append(" moves");
                for (int i = 0; i < nMoves; i++) {
                    posStr.append(" ");
                    posStr.append(TextIO.moveToUCIString(sr.mList.get(i)));
                }
            }
            uciEngine.writeLineToEngine(posStr.toString());
            uciEngine.setOption("UCI_AnalyseMode", true);
            uciEngine.setOption("Threads", sr.engineThreads > 0 ? sr.engineThreads : numCPUs);
            StringBuilder goStr = new StringBuilder(96);
            goStr.append("go infinite");
            if (sr.searchMoves != null) {
                goStr.append(" searchmoves");
                for (Move m : sr.searchMoves) {
                    goStr.append(' ');
                    goStr.append(TextIO.moveToUCIString(m));
                }
            }
            uciEngine.writeLineToEngine(goStr.toString());
            engineState.setState(MainState.ANALYZE);
        }
    }
