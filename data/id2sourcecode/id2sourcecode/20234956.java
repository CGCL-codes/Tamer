    public Score thinkMain(int x, int rt, int[] heights, int pieces[], int holdPiece[], boolean holdOK, int numPreviews) {
        Score score = new Score();
        int heightMin = 99;
        int heightMax = 0;
        ;
        int[] surface = ranks.heightsToSurface(heights);
        log.debug("piece id : " + pieces[0] + " rot : " + rt + " x :" + x + " surface :" + Arrays.toString(surface));
        boolean isVerticalIRightMost = (pieces[0] == Piece.PIECE_I && ((rt == 1) || (rt == 3)) && (x == 9));
        if (isVerticalIRightMost || ranks.surfaceFitsPiece(surface, pieces[0], rt, x)) {
            int[] heightsWork = heights.clone();
            if (!isVerticalIRightMost) {
                ranks.addToHeights(heightsWork, pieces[0], rt, x);
                for (int i = 0; i < ranks.getStackWidth(); i++) {
                    if (heightsWork[i] > heightMax) {
                        heightMax = heights[i];
                    }
                    if (heightsWork[i] < heightMin) {
                        heightMin = heights[i];
                    }
                }
            } else {
                for (int i = 0; i < ranks.getStackWidth(); i++) heightsWork[i] -= 4;
                heightMin -= 4;
                heightMax -= 4;
            }
            if (numPreviews > 0) {
                Score bestScore = new Score();
                Score scoreCurrent;
                int pieceNow = pieces[1];
                int[] pieces2 = new int[pieces.length];
                System.arraycopy(pieces, 1, pieces2, 0, pieces.length - 1);
                int[] holdPiece2 = new int[1];
                holdPiece2[0] = holdPiece[0];
                int numPreviews2 = numPreviews - 1;
                if (pieceNow == Piece.PIECE_I && heightMin >= THRESHOLD_FORCE_4LINES && heightMin >= 4) {
                    int rt2 = 1;
                    int maxX2 = ranks.getStackWidth() - 1;
                    scoreCurrent = thinkMain(maxX2 + 1, rt2, heightsWork, pieces2, holdPiece2, true, numPreviews2);
                    log.debug("SUB (4 Lines)" + numPreviews + " id=" + pieceNow + " posX=" + (maxX2 + 1) + " rt=" + rt2 + " score:" + scoreCurrent);
                    if (scoreCurrent.compareTo(bestScore) > 0) {
                        log.debug("SUB new best piece !");
                        bestScore = scoreCurrent;
                    }
                } else {
                    for (int h2 = 0; h2 < ((holdOK && allowHold) ? 2 : 1); h2++) {
                        if (h2 == 1) {
                            if (holdPiece2[0] == -1) {
                                holdPiece2[0] = pieces2[0];
                                for (int i = 0; i < pieces2.length - 1; i++) pieces2[i] = pieces2[i + 1];
                                pieceNow = pieces2[0];
                            } else {
                                int tempPiece = pieces2[0];
                                pieces2[0] = holdPiece2[0];
                                holdPiece2[0] = tempPiece;
                                pieceNow = pieces2[0];
                            }
                        }
                        for (int rt2 = 0; rt2 < Ranks.PIECES_NUM_ROTATIONS[pieceNow]; rt2++) {
                            boolean isVerticalI2 = (pieceNow == Piece.PIECE_I && ((rt2 == 1) || (rt2 == 3)));
                            int minX2 = 0;
                            int maxX2 = minX2 + ranks.getStackWidth() - Ranks.PIECES_WIDTHS[pieceNow][rt2];
                            for (int x2 = minX2; x2 <= maxX2; x2++) {
                                scoreCurrent = thinkMain(x2, rt2, heightsWork, pieces2, holdPiece2, (h2 == 1) ? false : true, numPreviews2);
                                log.debug("SUB " + numPreviews + " id=" + pieceNow + " posX=" + x2 + " rt=" + rt2 + " hold :" + h2 + " score " + scoreCurrent);
                                if (scoreCurrent.compareTo(bestScore) > 0) {
                                    log.debug("SUB new best piece !");
                                    bestScore = scoreCurrent;
                                }
                            }
                            if (isVerticalI2 && heightMin >= 4) {
                                scoreCurrent = thinkMain(maxX2 + 1, rt2, heightsWork, pieces2, holdPiece2, (h2 == 1) ? false : true, numPreviews2);
                                log.debug("SUB (4 Lines)" + numPreviews + " id=" + pieceNow + " posX=" + (maxX2 + 1) + " rt=" + rt2 + " hold :" + h2 + " score:" + scoreCurrent);
                                if (scoreCurrent.compareTo(bestScore) > 0) {
                                    log.debug("SUB new best piece !");
                                    bestScore = scoreCurrent;
                                }
                            }
                        }
                    }
                }
                return bestScore;
            } else {
                score.computeScore(heightsWork);
                if ((pieces[0] == Piece.PIECE_I) && (x < ranks.getStackWidth()) && numPreviews < MAX_PREVIEWS) {
                    score.iPieceUsedInTheStack = true;
                }
                return score;
            }
        } else {
            return score;
        }
    }
