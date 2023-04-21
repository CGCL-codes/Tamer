    protected Token getToken() throws IOException, LexerException {
        int dfa_state = 0;
        int start_pos = pos;
        int start_line = line;
        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;
        int[][][] gotoTable = this.gotoTable[state.id()];
        int[] accept = this.accept[state.id()];
        text.setLength(0);
        while (true) {
            int c = getChar();
            if (c != -1) {
                switch(c) {
                    case 10:
                        if (cr) {
                            cr = false;
                        } else {
                            line++;
                            pos = 0;
                        }
                        break;
                    case 13:
                        line++;
                        pos = 0;
                        cr = true;
                        break;
                    default:
                        pos++;
                        cr = false;
                        break;
                }
                ;
                text.append((char) c);
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
                    accept_length = text.length();
                    accept_pos = pos;
                    accept_line = line;
                }
            } else {
                if (accept_state != -1) {
                    switch(accept_token) {
                        case 0:
                            {
                                Token token = new0(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 1:
                            {
                                Token token = new1(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 2:
                            {
                                Token token = new2(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 3:
                            {
                                Token token = new3(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 4:
                            {
                                Token token = new4(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 5:
                            {
                                Token token = new5(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 6:
                            {
                                Token token = new6(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 7:
                            {
                                Token token = new7(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 8:
                            {
                                Token token = new8(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 9:
                            {
                                Token token = new9(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 10:
                            {
                                Token token = new10(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 11:
                            {
                                Token token = new11(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 12:
                            {
                                Token token = new12(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 13:
                            {
                                Token token = new13(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 14:
                            {
                                Token token = new14(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 15:
                            {
                                Token token = new15(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 16:
                            {
                                Token token = new16(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 17:
                            {
                                Token token = new17(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 18:
                            {
                                Token token = new18(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 19:
                            {
                                Token token = new19(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 20:
                            {
                                Token token = new20(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 21:
                            {
                                Token token = new21(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 22:
                            {
                                Token token = new22(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 23:
                            {
                                Token token = new23(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 24:
                            {
                                Token token = new24(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 25:
                            {
                                Token token = new25(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 26:
                            {
                                Token token = new26(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 27:
                            {
                                Token token = new27(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 28:
                            {
                                Token token = new28(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 29:
                            {
                                Token token = new29(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 30:
                            {
                                Token token = new30(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 31:
                            {
                                Token token = new31(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 32:
                            {
                                Token token = new32(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 33:
                            {
                                Token token = new33(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 34:
                            {
                                Token token = new34(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 35:
                            {
                                Token token = new35(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 36:
                            {
                                Token token = new36(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 37:
                            {
                                Token token = new37(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 38:
                            {
                                Token token = new38(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 39:
                            {
                                Token token = new39(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 40:
                            {
                                Token token = new40(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 41:
                            {
                                Token token = new41(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 42:
                            {
                                Token token = new42(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 43:
                            {
                                Token token = new43(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 44:
                            {
                                Token token = new44(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 45:
                            {
                                Token token = new45(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 46:
                            {
                                Token token = new46(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 47:
                            {
                                Token token = new47(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 48:
                            {
                                Token token = new48(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 49:
                            {
                                Token token = new49(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 50:
                            {
                                Token token = new50(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 51:
                            {
                                Token token = new51(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 52:
                            {
                                Token token = new52(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 53:
                            {
                                Token token = new53(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 54:
                            {
                                Token token = new54(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 55:
                            {
                                Token token = new55(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 56:
                            {
                                Token token = new56(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 57:
                            {
                                Token token = new57(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 58:
                            {
                                Token token = new58(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 59:
                            {
                                Token token = new59(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 60:
                            {
                                Token token = new60(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 61:
                            {
                                Token token = new61(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 62:
                            {
                                Token token = new62(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 63:
                            {
                                Token token = new63(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 64:
                            {
                                Token token = new64(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 65:
                            {
                                Token token = new65(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 66:
                            {
                                Token token = new66(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 67:
                            {
                                Token token = new67(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 68:
                            {
                                Token token = new68(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 69:
                            {
                                Token token = new69(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 70:
                            {
                                Token token = new70(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 71:
                            {
                                Token token = new71(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 72:
                            {
                                Token token = new72(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 73:
                            {
                                Token token = new73(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 74:
                            {
                                Token token = new74(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 75:
                            {
                                Token token = new75(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 76:
                            {
                                Token token = new76(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 77:
                            {
                                Token token = new77(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 78:
                            {
                                Token token = new78(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 79:
                            {
                                Token token = new79(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                        case 80:
                            {
                                Token token = new80(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                return token;
                            }
                    }
                } else {
                    if (text.length() > 0) {
                        throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "]" + " Unknown token: " + text);
                    } else {
                        EOF token = new EOF(start_line + 1, start_pos + 1);
                        return token;
                    }
                }
            }
        }
    }
