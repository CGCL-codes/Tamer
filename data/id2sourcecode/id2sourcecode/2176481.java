    private final int bishopEval(Position pos, int oldScore) {
        int score = 0;
        boolean whiteDark = false;
        boolean whiteLight = false;
        boolean blackDark = false;
        boolean blackLight = false;
        int nP = nPieces[Piece.WBISHOP];
        for (int i = 0; i < nP; i++) {
            int sq = pieces[Piece.WBISHOP][i];
            final int x = Position.getX(sq);
            final int y = Position.getY(sq);
            if (Position.darkSquare(x, y)) whiteDark = true; else whiteLight = true;
            score += bishopMobility(pos, x, y, sq) * 2;
        }
        nP = nPieces[Piece.BBISHOP];
        for (int i = 0; i < nP; i++) {
            int sq = pieces[Piece.BBISHOP][i];
            final int x = Position.getX(sq);
            final int y = Position.getY(sq);
            if (Position.darkSquare(x, y)) blackDark = true; else blackLight = true;
            score -= bishopMobility(pos, x, y, sq) * 2;
        }
        int numWhite = (whiteDark ? 1 : 0) + (whiteLight ? 1 : 0);
        int numBlack = (blackDark ? 1 : 0) + (blackLight ? 1 : 0);
        final int pV = pieceValue[Piece.WPAWN];
        if (numWhite == 2) {
            final int numPawns = pos.wMtrlPawns / pV;
            score += 20 + (8 - numPawns) * 2;
        }
        if (numBlack == 2) {
            final int numPawns = pos.bMtrlPawns / pV;
            score -= 20 + (8 - numPawns) * 2;
        }
        if ((numWhite == 1) && (numBlack == 1) && (whiteDark != blackDark)) {
            final int penalty = (oldScore + score) / 2;
            final int qV = pieceValue[Piece.WQUEEN];
            final int rV = pieceValue[Piece.WROOK];
            final int bV = pieceValue[Piece.WBISHOP];
            final int loMtrl = 2 * bV;
            final int hiMtrl = 2 * (qV + rV + bV);
            int mtrl = pos.wMtrl + pos.bMtrl - pos.wMtrlPawns - pos.bMtrlPawns;
            score -= interpolate(mtrl, loMtrl, penalty, hiMtrl, 0);
        }
        return score;
    }
