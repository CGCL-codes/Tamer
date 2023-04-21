    protected Token getToken() throws IOException, LexerException {
        int dfa_state = 0;
        int start_pos = this.pos;
        int start_line = this.line;
        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;
        @SuppressWarnings("hiding") int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
        @SuppressWarnings("hiding") int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);
        while (true) {
            int c = getChar();
            if (c != -1) {
                switch(c) {
                    case 10:
                        if (this.cr) {
                            this.cr = false;
                        } else {
                            this.line++;
                            this.pos = 0;
                        }
                        break;
                    case 13:
                        this.line++;
                        this.pos = 0;
                        this.cr = true;
                        break;
                    default:
                        this.pos++;
                        this.cr = false;
                        break;
                }
                this.text.append((char) c);
                do {
                    int oldState = (dfa_state < -1) ? (-2 - dfa_state) : dfa_state;
                    dfa_state = -1;
                    int[][] tmp1 = gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;
                    while (low <= high) {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];
                        if (c < tmp2[0]) {
                            high = middle - 1;
                        } else if (c > tmp2[1]) {
                            low = middle + 1;
                        } else {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                } while (dfa_state < -1);
            } else {
                dfa_state = -1;
            }
            if (dfa_state >= 0) {
                if (accept[dfa_state] != -1) {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            } else {
                if (accept_state != -1) {
                    switch(accept_token) {
                        case 0:
                            {
                                @SuppressWarnings("hiding") Token token = new0(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 1:
                            {
                                @SuppressWarnings("hiding") Token token = new1(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 2:
                            {
                                @SuppressWarnings("hiding") Token token = new2(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 3:
                            {
                                @SuppressWarnings("hiding") Token token = new3(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 4:
                            {
                                @SuppressWarnings("hiding") Token token = new4(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 5:
                            {
                                @SuppressWarnings("hiding") Token token = new5(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 6:
                            {
                                @SuppressWarnings("hiding") Token token = new6(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 7:
                            {
                                @SuppressWarnings("hiding") Token token = new7(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 8:
                            {
                                @SuppressWarnings("hiding") Token token = new8(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 9:
                            {
                                @SuppressWarnings("hiding") Token token = new9(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 10:
                            {
                                @SuppressWarnings("hiding") Token token = new10(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 11:
                            {
                                @SuppressWarnings("hiding") Token token = new11(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 12:
                            {
                                @SuppressWarnings("hiding") Token token = new12(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 13:
                            {
                                @SuppressWarnings("hiding") Token token = new13(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 14:
                            {
                                @SuppressWarnings("hiding") Token token = new14(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 15:
                            {
                                @SuppressWarnings("hiding") Token token = new15(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 16:
                            {
                                @SuppressWarnings("hiding") Token token = new16(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 17:
                            {
                                @SuppressWarnings("hiding") Token token = new17(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 18:
                            {
                                @SuppressWarnings("hiding") Token token = new18(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 19:
                            {
                                @SuppressWarnings("hiding") Token token = new19(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 20:
                            {
                                @SuppressWarnings("hiding") Token token = new20(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 21:
                            {
                                @SuppressWarnings("hiding") Token token = new21(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 22:
                            {
                                @SuppressWarnings("hiding") Token token = new22(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 23:
                            {
                                @SuppressWarnings("hiding") Token token = new23(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 24:
                            {
                                @SuppressWarnings("hiding") Token token = new24(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 25:
                            {
                                @SuppressWarnings("hiding") Token token = new25(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 26:
                            {
                                @SuppressWarnings("hiding") Token token = new26(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 27:
                            {
                                @SuppressWarnings("hiding") Token token = new27(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 28:
                            {
                                @SuppressWarnings("hiding") Token token = new28(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 29:
                            {
                                @SuppressWarnings("hiding") Token token = new29(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 30:
                            {
                                @SuppressWarnings("hiding") Token token = new30(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 31:
                            {
                                @SuppressWarnings("hiding") Token token = new31(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 32:
                            {
                                @SuppressWarnings("hiding") Token token = new32(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 33:
                            {
                                @SuppressWarnings("hiding") Token token = new33(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 34:
                            {
                                @SuppressWarnings("hiding") Token token = new34(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 35:
                            {
                                @SuppressWarnings("hiding") Token token = new35(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 36:
                            {
                                @SuppressWarnings("hiding") Token token = new36(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 37:
                            {
                                @SuppressWarnings("hiding") Token token = new37(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 38:
                            {
                                @SuppressWarnings("hiding") Token token = new38(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 39:
                            {
                                @SuppressWarnings("hiding") Token token = new39(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 40:
                            {
                                @SuppressWarnings("hiding") Token token = new40(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 41:
                            {
                                @SuppressWarnings("hiding") Token token = new41(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 42:
                            {
                                @SuppressWarnings("hiding") Token token = new42(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 43:
                            {
                                @SuppressWarnings("hiding") Token token = new43(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 44:
                            {
                                @SuppressWarnings("hiding") Token token = new44(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 45:
                            {
                                @SuppressWarnings("hiding") Token token = new45(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 46:
                            {
                                @SuppressWarnings("hiding") Token token = new46(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 47:
                            {
                                @SuppressWarnings("hiding") Token token = new47(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 48:
                            {
                                @SuppressWarnings("hiding") Token token = new48(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 49:
                            {
                                @SuppressWarnings("hiding") Token token = new49(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 50:
                            {
                                @SuppressWarnings("hiding") Token token = new50(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 51:
                            {
                                @SuppressWarnings("hiding") Token token = new51(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 52:
                            {
                                @SuppressWarnings("hiding") Token token = new52(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 53:
                            {
                                @SuppressWarnings("hiding") Token token = new53(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 54:
                            {
                                @SuppressWarnings("hiding") Token token = new54(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 55:
                            {
                                @SuppressWarnings("hiding") Token token = new55(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 56:
                            {
                                @SuppressWarnings("hiding") Token token = new56(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 57:
                            {
                                @SuppressWarnings("hiding") Token token = new57(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 58:
                            {
                                @SuppressWarnings("hiding") Token token = new58(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 59:
                            {
                                @SuppressWarnings("hiding") Token token = new59(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 60:
                            {
                                @SuppressWarnings("hiding") Token token = new60(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 61:
                            {
                                @SuppressWarnings("hiding") Token token = new61(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 62:
                            {
                                @SuppressWarnings("hiding") Token token = new62(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 63:
                            {
                                @SuppressWarnings("hiding") Token token = new63(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 64:
                            {
                                @SuppressWarnings("hiding") Token token = new64(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 65:
                            {
                                @SuppressWarnings("hiding") Token token = new65(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 66:
                            {
                                @SuppressWarnings("hiding") Token token = new66(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 67:
                            {
                                @SuppressWarnings("hiding") Token token = new67(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 68:
                            {
                                @SuppressWarnings("hiding") Token token = new68(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 69:
                            {
                                @SuppressWarnings("hiding") Token token = new69(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 70:
                            {
                                @SuppressWarnings("hiding") Token token = new70(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 71:
                            {
                                @SuppressWarnings("hiding") Token token = new71(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 72:
                            {
                                @SuppressWarnings("hiding") Token token = new72(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 73:
                            {
                                @SuppressWarnings("hiding") Token token = new73(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 74:
                            {
                                @SuppressWarnings("hiding") Token token = new74(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 75:
                            {
                                @SuppressWarnings("hiding") Token token = new75(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 76:
                            {
                                @SuppressWarnings("hiding") Token token = new76(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 77:
                            {
                                @SuppressWarnings("hiding") Token token = new77(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 78:
                            {
                                @SuppressWarnings("hiding") Token token = new78(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 79:
                            {
                                @SuppressWarnings("hiding") Token token = new79(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 80:
                            {
                                @SuppressWarnings("hiding") Token token = new80(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 81:
                            {
                                @SuppressWarnings("hiding") Token token = new81(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 82:
                            {
                                @SuppressWarnings("hiding") Token token = new82(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 83:
                            {
                                @SuppressWarnings("hiding") Token token = new83(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 84:
                            {
                                @SuppressWarnings("hiding") Token token = new84(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 85:
                            {
                                @SuppressWarnings("hiding") Token token = new85(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 86:
                            {
                                @SuppressWarnings("hiding") Token token = new86(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 87:
                            {
                                @SuppressWarnings("hiding") Token token = new87(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 88:
                            {
                                @SuppressWarnings("hiding") Token token = new88(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 89:
                            {
                                @SuppressWarnings("hiding") Token token = new89(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 90:
                            {
                                @SuppressWarnings("hiding") Token token = new90(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 91:
                            {
                                @SuppressWarnings("hiding") Token token = new91(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 92:
                            {
                                @SuppressWarnings("hiding") Token token = new92(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 93:
                            {
                                @SuppressWarnings("hiding") Token token = new93(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 94:
                            {
                                @SuppressWarnings("hiding") Token token = new94(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 95:
                            {
                                @SuppressWarnings("hiding") Token token = new95(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 96:
                            {
                                @SuppressWarnings("hiding") Token token = new96(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 97:
                            {
                                @SuppressWarnings("hiding") Token token = new97(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 98:
                            {
                                @SuppressWarnings("hiding") Token token = new98(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 99:
                            {
                                @SuppressWarnings("hiding") Token token = new99(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 100:
                            {
                                @SuppressWarnings("hiding") Token token = new100(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 101:
                            {
                                @SuppressWarnings("hiding") Token token = new101(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 102:
                            {
                                @SuppressWarnings("hiding") Token token = new102(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 103:
                            {
                                @SuppressWarnings("hiding") Token token = new103(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 104:
                            {
                                @SuppressWarnings("hiding") Token token = new104(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 105:
                            {
                                @SuppressWarnings("hiding") Token token = new105(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 106:
                            {
                                @SuppressWarnings("hiding") Token token = new106(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 107:
                            {
                                @SuppressWarnings("hiding") Token token = new107(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 108:
                            {
                                @SuppressWarnings("hiding") Token token = new108(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 109:
                            {
                                @SuppressWarnings("hiding") Token token = new109(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 110:
                            {
                                @SuppressWarnings("hiding") Token token = new110(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 111:
                            {
                                @SuppressWarnings("hiding") Token token = new111(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 112:
                            {
                                @SuppressWarnings("hiding") Token token = new112(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 113:
                            {
                                @SuppressWarnings("hiding") Token token = new113(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 114:
                            {
                                @SuppressWarnings("hiding") Token token = new114(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 115:
                            {
                                @SuppressWarnings("hiding") Token token = new115(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 116:
                            {
                                @SuppressWarnings("hiding") Token token = new116(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 117:
                            {
                                @SuppressWarnings("hiding") Token token = new117(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 118:
                            {
                                @SuppressWarnings("hiding") Token token = new118(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 119:
                            {
                                @SuppressWarnings("hiding") Token token = new119(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                    }
                } else {
                    if (this.text.length() > 0) {
                        throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "]" + " Unknown token: " + this.text);
                    }
                    @SuppressWarnings("hiding") EOF token = new EOF(start_line + 1, start_pos + 1);
                    return token;
                }
            }
        }
    }
