    final int getToken() throws IOException {
        int c;
        retry: for (; ; ) {
            for (; ; ) {
                c = getChar();
                if (c == EOF_CHAR) {
                    tokenBeg = cursor - 1;
                    tokenEnd = cursor;
                    return Token.EOF;
                } else if (c == '\n') {
                    dirtyLine = false;
                    tokenBeg = cursor - 1;
                    tokenEnd = cursor;
                    return Token.EOL;
                } else if (!isJSSpace(c)) {
                    if (c != '-') {
                        dirtyLine = true;
                    }
                    break;
                }
            }
            tokenBeg = cursor - 1;
            tokenEnd = cursor;
            if (c == '@') return Token.XMLATTR;
            boolean identifierStart;
            boolean isUnicodeEscapeStart = false;
            if (c == '\\') {
                c = getChar();
                if (c == 'u') {
                    identifierStart = true;
                    isUnicodeEscapeStart = true;
                    stringBufferTop = 0;
                } else {
                    identifierStart = false;
                    ungetChar(c);
                    c = '\\';
                }
            } else {
                identifierStart = Character.isJavaIdentifierStart((char) c);
                if (identifierStart) {
                    stringBufferTop = 0;
                    addToString(c);
                }
            }
            if (identifierStart) {
                boolean containsEscape = isUnicodeEscapeStart;
                for (; ; ) {
                    if (isUnicodeEscapeStart) {
                        int escapeVal = 0;
                        for (int i = 0; i != 4; ++i) {
                            c = getChar();
                            escapeVal = Kit.xDigitToInt(c, escapeVal);
                            if (escapeVal < 0) {
                                break;
                            }
                        }
                        if (escapeVal < 0) {
                            parser.addError("msg.invalid.escape");
                            return Token.ERROR;
                        }
                        addToString(escapeVal);
                        isUnicodeEscapeStart = false;
                    } else {
                        c = getChar();
                        if (c == '\\') {
                            c = getChar();
                            if (c == 'u') {
                                isUnicodeEscapeStart = true;
                                containsEscape = true;
                            } else {
                                parser.addError("msg.illegal.character");
                                return Token.ERROR;
                            }
                        } else {
                            if (c == EOF_CHAR || c == BYTE_ORDER_MARK || !Character.isJavaIdentifierPart((char) c)) {
                                break;
                            }
                            addToString(c);
                        }
                    }
                }
                ungetChar(c);
                String str = getStringFromBuffer();
                if (!containsEscape) {
                    int result = stringToKeyword(str);
                    if (result != Token.EOF) {
                        if ((result == Token.LET || result == Token.YIELD) && parser.compilerEnv.getLanguageVersion() < Context.VERSION_1_7) {
                            string = result == Token.LET ? "let" : "yield";
                            result = Token.NAME;
                        }
                        if (result != Token.RESERVED) {
                            return result;
                        } else if (!parser.compilerEnv.isReservedKeywordAsIdentifier()) {
                            return result;
                        }
                    }
                }
                this.string = (String) allStrings.intern(str);
                return Token.NAME;
            }
            if (isDigit(c) || (c == '.' && isDigit(peekChar()))) {
                isOctal = false;
                stringBufferTop = 0;
                int base = 10;
                if (c == '0') {
                    c = getChar();
                    if (c == 'x' || c == 'X') {
                        base = 16;
                        c = getChar();
                    } else if (isDigit(c)) {
                        base = 8;
                        isOctal = true;
                    } else {
                        addToString('0');
                    }
                }
                if (base == 16) {
                    while (0 <= Kit.xDigitToInt(c, 0)) {
                        addToString(c);
                        c = getChar();
                    }
                } else {
                    while ('0' <= c && c <= '9') {
                        if (base == 8 && c >= '8') {
                            parser.addWarning("msg.bad.octal.literal", c == '8' ? "8" : "9");
                            base = 10;
                        }
                        addToString(c);
                        c = getChar();
                    }
                }
                boolean isInteger = true;
                if (base == 10 && (c == '.' || c == 'e' || c == 'E')) {
                    isInteger = false;
                    if (c == '.') {
                        do {
                            addToString(c);
                            c = getChar();
                        } while (isDigit(c));
                    }
                    if (c == 'e' || c == 'E') {
                        addToString(c);
                        c = getChar();
                        if (c == '+' || c == '-') {
                            addToString(c);
                            c = getChar();
                        }
                        if (!isDigit(c)) {
                            parser.addError("msg.missing.exponent");
                            return Token.ERROR;
                        }
                        do {
                            addToString(c);
                            c = getChar();
                        } while (isDigit(c));
                    }
                }
                ungetChar(c);
                String numString = getStringFromBuffer();
                this.string = numString;
                double dval;
                if (base == 10 && !isInteger) {
                    try {
                        dval = Double.valueOf(numString).doubleValue();
                    } catch (NumberFormatException ex) {
                        parser.addError("msg.caught.nfe");
                        return Token.ERROR;
                    }
                } else {
                    dval = ScriptRuntime.stringToNumber(numString, 0, base);
                }
                this.number = dval;
                return Token.NUMBER;
            }
            if (c == '"' || c == '\'') {
                quoteChar = c;
                stringBufferTop = 0;
                c = getChar();
                strLoop: while (c != quoteChar) {
                    if (c == '\n' || c == EOF_CHAR) {
                        ungetChar(c);
                        tokenEnd = cursor;
                        parser.addError("msg.unterminated.string.lit");
                        return Token.ERROR;
                    }
                    if (c == '\\') {
                        int escapeVal;
                        c = getChar();
                        switch(c) {
                            case 'b':
                                c = '\b';
                                break;
                            case 'f':
                                c = '\f';
                                break;
                            case 'n':
                                c = '\n';
                                break;
                            case 'r':
                                c = '\r';
                                break;
                            case 't':
                                c = '\t';
                                break;
                            case 'v':
                                c = 0xb;
                                break;
                            case 'u':
                                int escapeStart = stringBufferTop;
                                addToString('u');
                                escapeVal = 0;
                                for (int i = 0; i != 4; ++i) {
                                    c = getChar();
                                    escapeVal = Kit.xDigitToInt(c, escapeVal);
                                    if (escapeVal < 0) {
                                        continue strLoop;
                                    }
                                    addToString(c);
                                }
                                stringBufferTop = escapeStart;
                                c = escapeVal;
                                break;
                            case 'x':
                                c = getChar();
                                escapeVal = Kit.xDigitToInt(c, 0);
                                if (escapeVal < 0) {
                                    addToString('x');
                                    continue strLoop;
                                } else {
                                    int c1 = c;
                                    c = getChar();
                                    escapeVal = Kit.xDigitToInt(c, escapeVal);
                                    if (escapeVal < 0) {
                                        addToString('x');
                                        addToString(c1);
                                        continue strLoop;
                                    } else {
                                        c = escapeVal;
                                    }
                                }
                                break;
                            case '\n':
                                c = getChar();
                                continue strLoop;
                            default:
                                if ('0' <= c && c < '8') {
                                    int val = c - '0';
                                    c = getChar();
                                    if ('0' <= c && c < '8') {
                                        val = 8 * val + c - '0';
                                        c = getChar();
                                        if ('0' <= c && c < '8' && val <= 037) {
                                            val = 8 * val + c - '0';
                                            c = getChar();
                                        }
                                    }
                                    ungetChar(c);
                                    c = val;
                                }
                        }
                    }
                    addToString(c);
                    c = getChar();
                }
                String str = getStringFromBuffer();
                this.string = (String) allStrings.intern(str);
                return Token.STRING;
            }
            switch(c) {
                case ';':
                    return Token.SEMI;
                case '[':
                    return Token.LB;
                case ']':
                    return Token.RB;
                case '{':
                    return Token.LC;
                case '}':
                    return Token.RC;
                case '(':
                    return Token.LP;
                case ')':
                    return Token.RP;
                case ',':
                    return Token.COMMA;
                case '?':
                    return Token.HOOK;
                case ':':
                    if (matchChar(':')) {
                        return Token.COLONCOLON;
                    } else {
                        return Token.COLON;
                    }
                case '.':
                    if (matchChar('.')) {
                        return Token.DOTDOT;
                    } else if (matchChar('(')) {
                        return Token.DOTQUERY;
                    } else {
                        return Token.DOT;
                    }
                case '|':
                    if (matchChar('|')) {
                        return Token.OR;
                    } else if (matchChar('=')) {
                        return Token.ASSIGN_BITOR;
                    } else {
                        return Token.BITOR;
                    }
                case '^':
                    if (matchChar('=')) {
                        return Token.ASSIGN_BITXOR;
                    } else {
                        return Token.BITXOR;
                    }
                case '&':
                    if (matchChar('&')) {
                        return Token.AND;
                    } else if (matchChar('=')) {
                        return Token.ASSIGN_BITAND;
                    } else {
                        return Token.BITAND;
                    }
                case '=':
                    if (matchChar('=')) {
                        if (matchChar('=')) {
                            return Token.SHEQ;
                        } else {
                            return Token.EQ;
                        }
                    } else {
                        return Token.ASSIGN;
                    }
                case '!':
                    if (matchChar('=')) {
                        if (matchChar('=')) {
                            return Token.SHNE;
                        } else {
                            return Token.NE;
                        }
                    } else {
                        return Token.NOT;
                    }
                case '<':
                    if (matchChar('!')) {
                        if (matchChar('-')) {
                            if (matchChar('-')) {
                                skipLine();
                                continue retry;
                            }
                            ungetCharIgnoreLineEnd('-');
                        }
                        ungetCharIgnoreLineEnd('!');
                    }
                    if (matchChar('<')) {
                        if (matchChar('=')) {
                            return Token.ASSIGN_LSH;
                        } else {
                            return Token.LSH;
                        }
                    } else {
                        if (matchChar('=')) {
                            return Token.LE;
                        } else {
                            return Token.LT;
                        }
                    }
                case '>':
                    if (matchChar('>')) {
                        if (matchChar('>')) {
                            if (matchChar('=')) {
                                return Token.ASSIGN_URSH;
                            } else {
                                return Token.URSH;
                            }
                        } else {
                            if (matchChar('=')) {
                                return Token.ASSIGN_RSH;
                            } else {
                                return Token.RSH;
                            }
                        }
                    } else {
                        if (matchChar('=')) {
                            return Token.GE;
                        } else {
                            return Token.GT;
                        }
                    }
                case '*':
                    if (matchChar('=')) {
                        return Token.ASSIGN_MUL;
                    } else {
                        return Token.MUL;
                    }
                case '/':
                    markCommentStart();
                    if (matchChar('/')) {
                        tokenBeg = cursor - 2;
                        skipLine();
                        commentType = Token.CommentType.LINE;
                        return Token.COMMENT;
                    }
                    if (matchChar('*')) {
                        boolean lookForSlash = false;
                        tokenBeg = cursor - 2;
                        if (matchChar('*')) {
                            lookForSlash = true;
                            commentType = Token.CommentType.JSDOC;
                        } else {
                            commentType = Token.CommentType.BLOCK_COMMENT;
                        }
                        for (; ; ) {
                            c = getChar();
                            if (c == EOF_CHAR) {
                                tokenEnd = cursor - 1;
                                parser.addError("msg.unterminated.comment");
                                return Token.COMMENT;
                            } else if (c == '*') {
                                lookForSlash = true;
                            } else if (c == '/') {
                                if (lookForSlash) {
                                    tokenEnd = cursor;
                                    return Token.COMMENT;
                                }
                            } else {
                                lookForSlash = false;
                                tokenEnd = cursor;
                            }
                        }
                    }
                    if (matchChar('=')) {
                        return Token.ASSIGN_DIV;
                    } else {
                        return Token.DIV;
                    }
                case '%':
                    if (matchChar('=')) {
                        return Token.ASSIGN_MOD;
                    } else {
                        return Token.MOD;
                    }
                case '~':
                    return Token.BITNOT;
                case '+':
                    if (matchChar('=')) {
                        return Token.ASSIGN_ADD;
                    } else if (matchChar('+')) {
                        return Token.INC;
                    } else {
                        return Token.ADD;
                    }
                case '-':
                    if (matchChar('=')) {
                        c = Token.ASSIGN_SUB;
                    } else if (matchChar('-')) {
                        if (!dirtyLine) {
                            if (matchChar('>')) {
                                markCommentStart("--");
                                skipLine();
                                commentType = Token.CommentType.HTML;
                                return Token.COMMENT;
                            }
                        }
                        c = Token.DEC;
                    } else {
                        c = Token.SUB;
                    }
                    dirtyLine = true;
                    return c;
                default:
                    parser.addError("msg.illegal.character");
                    return Token.ERROR;
            }
        }
    }
