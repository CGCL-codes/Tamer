        void scan() throws BadLocationException {
            while (ch != EOF) {
                if (slc) {
                    if (ch == '\n' || ch == '\r') {
                        slc = false;
                    }
                } else if (mlc > 0) {
                    if (last == '*' && ch == '/') {
                        mlc--;
                    } else if (last == '/' && ch == '*') {
                        mlc++;
                    }
                } else if (str) {
                    if (ch == '"') {
                        str = false;
                    }
                } else if (chr) {
                    if (ch == '\'') {
                        chr = false;
                    }
                } else {
                    switch(ch) {
                        case '/':
                            if (last == '/') {
                                slc = true;
                            }
                            break;
                        case '*':
                            if (last == '/') {
                                mlc++;
                            }
                            break;
                        case '"':
                            str = true;
                            break;
                        case '\'':
                            chr = true;
                            break;
                        case '.':
                            if (last == '(') {
                                ilc = true;
                            }
                            break;
                        case ')':
                            if (last == '.') {
                                semActEnd = curPos - 1;
                                javaStartPos = addSemanticRegion(semActBegin, semActEnd - semActBegin, javaStartPos);
                                sa = false;
                                ilc = false;
                            }
                            break;
                        case '\n':
                            if (sa) {
                                semActEnd = curPos;
                                javaStartPos = addSemanticRegion(semActBegin, semActEnd - semActBegin, javaStartPos);
                                openpar = true;
                            }
                        default:
                            if (ilc && !sa) {
                                if (Character.isWhitespace(ch)) {
                                    break;
                                } else {
                                    semActBegin = curPos;
                                    sa = true;
                                }
                            }
                            if (openpar) {
                                if (Character.isWhitespace(ch)) {
                                    break;
                                }
                                semActBegin = curPos;
                                openpar = false;
                            }
                            break;
                    }
                }
                last = ch;
                nextCh();
            }
        }
