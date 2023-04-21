    public int computerMove(IProgressMonitor progMonitor) {
        if (gameOver()) {
            return (whoWon);
        }
        if (!computerTurn) return (INVALID_MOVE);
        int maxScore = -322000;
        int bestPlay = -1;
        if (!firstMove && level != 0) {
            try {
                for (int col = 0; col <= COL_MAX; col++) {
                    if (validMove(col)) {
                        int[] testBoard = copyBoard(gameState);
                        int score = 0;
                        score = calculateComputerChances(col, testBoard, level == 1 ? 2 : MAX_LEVEL);
                        progMonitor.worked(col);
                        if (score > maxScore && makeMove(COMPUTER_PIECE, col, gameState) != -1) {
                            removePiece(col, gameState);
                            maxScore = score;
                            bestPlay = col;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Problem:" + ex);
            }
        } else {
            firstMove = false;
        }
        int row, col;
        if (bestPlay != -1) {
            col = bestPlay;
            row = makeMove(COMPUTER_PIECE, bestPlay, gameState);
        } else {
            Random rand = new Random();
            col = rand.nextInt(COL_MAX + 1);
            while ((row = makeMove(COMPUTER_PIECE, col, gameState)) == -1) {
                col = rand.nextInt(COL_MAX + 1);
            }
        }
        lastRow = row;
        lastCol = col;
        if (checkForWin(COMPUTER_PIECE, gameState) == COMPUTER_WIN) {
            gameOver = true;
            whoWon = COMPUTER_WIN;
            return (COMPUTER_WIN);
        }
        computerTurn = false;
        if (gameOver()) return (whoWon); else return (NO_WINYET);
    }
