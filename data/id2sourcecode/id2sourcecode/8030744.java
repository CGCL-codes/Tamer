    static int isMoveLegal(int[][] board, int srtI, int srtJ, int endI, int endJ, int turn) {
        if (!(inRange(srtI, srtJ) && inRange(endI, endJ))) return illegalMove;
        if (board[endI][endJ] != Checkers.empty) return illegalMove;
        int piece = board[srtI][srtJ];
        if (Math.abs(srtI - endI) == 1) {
            switch(piece) {
                case Checkers.redNormal:
                case Checkers.redKing:
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if ((board[i][j] == Checkers.redNormal || board[i][j] == Checkers.redKing) && canCapture(board, i, j)) return illegalMove;
                        }
                    }
                    break;
                case Checkers.yellowNormal:
                case Checkers.yellowKing:
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if ((board[i][j] == Checkers.yellowNormal || board[i][j] == Checkers.yellowKing) && canCapture(board, i, j)) return illegalMove;
                        }
                    }
                    break;
            }
            switch(piece) {
                case Checkers.redNormal:
                    if (endJ - srtJ == 1) return legalMove;
                    break;
                case Checkers.yellowNormal:
                    if (endJ - srtJ == -1) return legalMove;
                    break;
                case Checkers.redKing:
                case Checkers.yellowKing:
                    if (Math.abs(endJ - srtJ) == 1) return legalMove;
                    break;
            }
            return illegalMove;
        } else if (Math.abs(srtI - endI) == 2) {
            int cap_i = (srtI + endI) / 2;
            int cap_j = (srtJ + endJ) / 2;
            int cap_piece = board[cap_i][cap_j];
            if (turn == Checkers.redNormal) {
                if (!(cap_piece == Checkers.yellowNormal || cap_piece == Checkers.yellowKing)) return illegalMove;
            } else if (!(cap_piece == Checkers.redNormal || cap_piece == Checkers.redKing)) return illegalMove;
            switch(piece) {
                case Checkers.redNormal:
                    if (endJ - srtJ != 2) return illegalMove;
                    break;
                case Checkers.yellowNormal:
                    if (endJ - srtJ != -2) return illegalMove;
                    break;
                case Checkers.redKing:
                case Checkers.yellowKing:
                    if (Math.abs(endJ - srtJ) != 2) return illegalMove;
            }
            return incompleteMove;
        }
        return illegalMove;
    }
