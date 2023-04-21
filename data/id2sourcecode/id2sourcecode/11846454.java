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
                                @SuppressWarnings("hiding") Token token = new0(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 1:
                            {
                                @SuppressWarnings("hiding") Token token = new1(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 2:
                            {
                                @SuppressWarnings("hiding") Token token = new2(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 3:
                            {
                                @SuppressWarnings("hiding") Token token = new3(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 4:
                            {
                                @SuppressWarnings("hiding") Token token = new4(getText(accept_length), start_line + 1, start_pos + 1);
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
                                @SuppressWarnings("hiding") Token token = new31(getText(accept_length), start_line + 1, start_pos + 1);
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
                                @SuppressWarnings("hiding") Token token = new44(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 45:
                            {
                                @SuppressWarnings("hiding") Token token = new45(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 46:
                            {
                                @SuppressWarnings("hiding") Token token = new46(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                return token;
                            }
                        case 47:
                            {
                                @SuppressWarnings("hiding") Token token = new47(getText(accept_length), start_line + 1, start_pos + 1);
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
