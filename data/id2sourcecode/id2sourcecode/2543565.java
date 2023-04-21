    public void cleanPhpBlock(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        writer.write(reader.read(5));
        String next2 = reader.readAheadSkipWhitespace(2);
        boolean needsWhitespace = false;
        if (!next2.equals("//") && !next2.equals("/*")) {
            needsWhitespace = true;
        }
        writer.indentIncrease();
        boolean needsLineBefore = false;
        boolean inInlineBrace = false;
        boolean inBracket = false;
        int charBeforeBlockComment = -1;
        int cur = -1;
        int prev = ' ';
        int prevNonWhitespace = -1;
        boolean isOnBlankLine = false;
        while ((cur = reader.read()) != -1) {
            if (cur == '?' && reader.readAhead() == '>') {
                writer.write(' ');
                writer.write(cur);
                writer.write(reader.read());
                if (prevNonWhitespace == '{') {
                    writer.newLineMaybe();
                    writer.indentIncrease();
                    needsWhitespace = false;
                    inInlineBrace = false;
                    prevNonWhitespace = -5;
                }
                writer.indentDecrease();
                return;
            }
            if (cur == '/' && reader.readAhead() == '/') {
                if (!isOnBlankLine && (prevNonWhitespace == ';')) {
                    writer.write(' ');
                    needsWhitespace = false;
                }
                if (prevNonWhitespace == '{') {
                    writer.newLine();
                    writer.indentIncrease();
                } else if (prevNonWhitespace == -1) {
                    writer.newLine();
                } else if (prevNonWhitespace == '}') {
                    writer.newLine();
                } else if (isOnBlankLine) {
                    writer.newLineMaybe();
                    isOnBlankLine = false;
                }
                writer.write(cur);
                writer.write(reader.read());
                jumpOverInlineComment(reader, writer, false);
                needsLineBefore = true;
                prevNonWhitespace = -3;
                inInlineBrace = false;
                continue;
            }
            if (cur == '/' && reader.readAhead() == '*') {
                if (prevNonWhitespace == '{') {
                    writer.newLine();
                    writer.indentIncrease();
                } else if (isOnBlankLine && prevNonWhitespace == '/') {
                    writer.write(' ');
                } else if (!isOnBlankLine && prevNonWhitespace != '(' && prevNonWhitespace != -1 && prevNonWhitespace != -3) {
                    writer.write(' ');
                } else if (prevNonWhitespace == ';' || prevNonWhitespace == -1 || prevNonWhitespace == -2 || prevNonWhitespace == -1 || prevNonWhitespace == '}') {
                    writer.newLine();
                }
                writer.write(cur);
                writer.write(reader.read());
                jumpOverBlockComment(reader, writer, false);
                needsWhitespace = true;
                charBeforeBlockComment = prevNonWhitespace;
                prevNonWhitespace = -2;
                inInlineBrace = false;
                continue;
            }
            if (cur == '\n' || cur == '\r') {
                isOnBlankLine = true;
            }
            if (Character.isWhitespace(cur) && shouldIgnoreWhitespaceAfter(prev)) {
                if (needsWhitespaceCharacterAfter(prev)) {
                    needsWhitespace = true;
                }
            } else if (Character.isWhitespace(cur) && Character.isWhitespace(prev)) {
            } else if (Character.isWhitespace(cur) && !Character.isWhitespace(prev)) {
                if (prev != '[' && prev != '!') {
                    needsWhitespace = true;
                }
            } else if (Character.isWhitespace(cur)) {
            } else {
                if (needsLineBefore) {
                    writer.newLineMaybe();
                    needsLineBefore = false;
                }
                isOnBlankLine = false;
                if (prevNonWhitespace == ';') {
                    if (inBracket) {
                        writer.write(' ');
                    } else {
                        writer.newLine();
                    }
                } else if (prevNonWhitespace == -2 && cur != ';' && cur != ',' && cur != ')' && cur != '{' && !Character.isWhitespace(cur) && charBeforeBlockComment != '(') {
                    writer.newLineMaybe();
                    needsWhitespace = false;
                } else if (prevNonWhitespace == '{') {
                    writer.newLineMaybe();
                    writer.indentIncrease();
                    needsWhitespace = false;
                    inInlineBrace = false;
                } else if (prevNonWhitespace == '}') {
                    if (cur == ',' || cur == ')') {
                    } else if (!isNextWordInlineBrace(reader, writer)) {
                        writer.newLine();
                    } else {
                        writer.write(' ');
                        inInlineBrace = true;
                    }
                } else if (prevNonWhitespace == ']' && (cur == ')')) {
                } else if (needsWhitespaceBetweenPhp(reader, writer, prevNonWhitespace, cur)) {
                    writer.write(' ');
                    needsWhitespace = false;
                } else if (needsWhitespace) {
                    if (!doesntActuallyNeedWhitespaceBeforePhp(reader, writer, cur) && prevNonWhitespace != -3) {
                        if (writer.getPrevious() != '\n') {
                            writer.write(' ');
                        }
                    }
                    needsWhitespace = false;
                }
                if (cur == '}') {
                    writer.indentDecrease();
                    if (prevNonWhitespace != -1) {
                        writer.newLineMaybe();
                    } else {
                        writer.write(' ');
                    }
                }
                if (cur == '(' && inInlineBrace) {
                    writer.write(' ');
                }
                writer.write(cur);
                if (cur == ';') {
                    inInlineBrace = false;
                }
                if (cur == '(') {
                    inBracket = true;
                } else if (cur == ')') {
                    inBracket = false;
                }
                if (cur == '"') {
                    jumpOverString(reader, writer, false);
                } else if (cur == '\'') {
                    jumpOverSingleString(reader, writer, false);
                }
                if (!Character.isWhitespace(cur)) {
                    prevNonWhitespace = cur;
                }
            }
            prev = cur;
        }
        if (prevNonWhitespace == '{') {
            writer.newLineMaybe();
            writer.indentIncrease();
        }
        writer.indentDecrease();
    }
