    private final int bishopEval(Position pos, int oldScore) {
        int score = 0;
        final long occupied = pos.whiteBB | pos.blackBB;
        long wBishops = pos.pieceTypeBB[Piece.WBISHOP];
        long bBishops = pos.pieceTypeBB[Piece.BBISHOP];
        if ((wBishops | bBishops) == 0) return 0;
        long m = wBishops;
        while (m != 0) {
            int sq = BitBoard.numberOfTrailingZeros(m);
            long atk = BitBoard.bishopAttacks(sq, occupied);
            wAttacksBB |= atk;
            score += bishMobScore[Long.bitCount(atk & ~(pos.whiteBB | bPawnAttacks))];
            if ((atk & bKingZone) != 0) bKingAttacks += Long.bitCount(atk & bKingZone);
            m &= m - 1;
        }
        m = bBishops;
        while (m != 0) {
            int sq = BitBoard.numberOfTrailingZeros(m);
            long atk = BitBoard.bishopAttacks(sq, occupied);
            bAttacksBB |= atk;
            score -= bishMobScore[Long.bitCount(atk & ~(pos.blackBB | wPawnAttacks))];
            if ((atk & wKingZone) != 0) wKingAttacks += Long.bitCount(atk & wKingZone);
            m &= m - 1;
        }
        boolean whiteDark = (pos.pieceTypeBB[Piece.WBISHOP] & BitBoard.maskDarkSq) != 0;
        boolean whiteLight = (pos.pieceTypeBB[Piece.WBISHOP] & BitBoard.maskLightSq) != 0;
        boolean blackDark = (pos.pieceTypeBB[Piece.BBISHOP] & BitBoard.maskDarkSq) != 0;
        boolean blackLight = (pos.pieceTypeBB[Piece.BBISHOP] & BitBoard.maskLightSq) != 0;
        int numWhite = (whiteDark ? 1 : 0) + (whiteLight ? 1 : 0);
        int numBlack = (blackDark ? 1 : 0) + (blackLight ? 1 : 0);
        if (numWhite == 2) {
            final int numPawns = pos.wMtrlPawns / pV;
            score += 28 + (8 - numPawns) * 3;
        }
        if (numBlack == 2) {
            final int numPawns = pos.bMtrlPawns / pV;
            score -= 28 + (8 - numPawns) * 3;
        }
        if ((numWhite == 1) && (numBlack == 1) && (whiteDark != blackDark) && (pos.wMtrl - pos.wMtrlPawns == pos.bMtrl - pos.bMtrlPawns)) {
            final int penalty = (oldScore + score) / 2;
            final int loMtrl = 2 * bV;
            final int hiMtrl = 2 * (qV + rV + bV);
            int mtrl = pos.wMtrl + pos.bMtrl - pos.wMtrlPawns - pos.bMtrlPawns;
            score -= interpolate(mtrl, loMtrl, penalty, hiMtrl, 0);
        }
        if (((wBishops | bBishops) & 0x0081000000008100L) != 0) {
            if ((pos.squares[48] == Piece.WBISHOP) && (pos.squares[41] == Piece.BPAWN) && (pos.squares[50] == Piece.BPAWN)) score -= pV * 3 / 2;
            if ((pos.squares[55] == Piece.WBISHOP) && (pos.squares[46] == Piece.BPAWN) && (pos.squares[53] == Piece.BPAWN)) score -= (pos.pieceTypeBB[Piece.WQUEEN] != 0) ? pV : pV * 3 / 2;
            if ((pos.squares[8] == Piece.BBISHOP) && (pos.squares[17] == Piece.WPAWN) && (pos.squares[10] == Piece.WPAWN)) score += pV * 3 / 2;
            if ((pos.squares[15] == Piece.BBISHOP) && (pos.squares[22] == Piece.WPAWN) && (pos.squares[13] == Piece.WPAWN)) score += (pos.pieceTypeBB[Piece.BQUEEN] != 0) ? pV : pV * 3 / 2;
        }
        return score;
    }
