    public void genmove() {
        deep++;
        ababort = false;
        if (deep % 2 != 0) {
            minimax[deep] = 2000.0F;
            alphabeta[deep] = 3000.0F;
        } else {
            minimax[deep] = -2000.0F;
            alphabeta[deep] = -3000.0F;
        }
        for (int i = 21; i < 99; i++) {
            if (board[i] % 100 / 10 == color) {
                switch(board[i] % 10) {
                    case 1:
                        if (color == 1) {
                            if (board[i - 10] == 0) {
                                simulize(i, i - 10);
                            }
                            if (board[i - 9] % 100 / 10 == 2) {
                                simulize(i, i - 9);
                            }
                            if (board[i - 11] % 100 / 10 == 2) {
                                simulize(i, i - 11);
                            }
                            if ((i > 80) && ((board[i - 10] == 0) && (board[i - 20] == 0))) {
                                simulize(i, i - 20);
                            }
                        } else {
                            if (board[i + 10] == 0) {
                                simulize(i, i + 10);
                            }
                            if (board[i + 9] % 100 / 10 == 1) {
                                simulize(i, i + 9);
                            }
                            if (board[i + 11] % 100 / 10 == 1) {
                                simulize(i, i + 11);
                            }
                            if ((i < 39) && ((board[i + 10] == 0) && (board[i + 20] == 0))) {
                                simulize(i, i + 20);
                            }
                        }
                        break;
                    case 2:
                        simulize(i, i + 12);
                        simulize(i, i - 12);
                        simulize(i, i + 21);
                        simulize(i, i - 21);
                        simulize(i, i + 19);
                        simulize(i, i - 19);
                        simulize(i, i + 8);
                        simulize(i, i - 8);
                        break;
                    case 5:
                    case 3:
                        multisimulize(i, -9);
                        multisimulize(i, -11);
                        multisimulize(i, +9);
                        multisimulize(i, +11);
                        if (board[i] % 10 == 3) {
                            break;
                        }
                    case 4:
                        multisimulize(i, -10);
                        multisimulize(i, +10);
                        multisimulize(i, -1);
                        multisimulize(i, +1);
                        break;
                    case 6:
                        if ((board[i] / 100 == 1) && (!ischeck())) {
                            if (((board[i + 1] == 0) && (board[i + 2] == 0)) && (board[i + 3] / 100 == 1)) {
                                board[i + 1] = board[i] % 100;
                                board[i] = 0;
                                if (!ischeck()) {
                                    board[i] = board[i + 1];
                                    board[i + 1] = board[i + 3] % 100;
                                    board[i + 3] = 0;
                                    simulize(i, i + 2);
                                    board[i + 3] = board[i + 1] + 100;
                                    board[i + 1] = board[i];
                                }
                                board[i] = board[i + 1] + 100;
                                board[i + 1] = 0;
                            }
                            if (((board[i - 1] == 0) && (board[i - 2] == 0)) && ((board[i - 3] == 0) && (board[i - 4] / 100 == 1))) {
                                board[i - 1] = board[i] % 100;
                                board[i] = 0;
                                if (!ischeck()) {
                                    board[i] = board[i - 1];
                                    board[i - 1] = board[i - 4] % 100;
                                    board[i - 4] = 0;
                                    simulize(i, i - 2);
                                    board[i - 4] = board[i - 1] + 100;
                                    board[i - 1] = board[i];
                                }
                                board[i] = board[i - 1] + 100;
                                board[i - 1] = 0;
                            }
                        }
                        simulize(i, i + 1);
                        simulize(i, i - 1);
                        simulize(i, i + 10);
                        simulize(i, i - 10);
                        simulize(i, i + 9);
                        simulize(i, i - 9);
                        simulize(i, i + 11);
                        simulize(i, i - 11);
                }
            }
            if (i % 10 == 8) {
                i += 2;
            }
        }
        deep--;
        ababort = false;
    }
