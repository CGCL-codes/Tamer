    public void cleanJavascriptBlock(InlineStringReader reader, InlineStringWriter writer, boolean withinHtml) throws IOException, CleanerException {
        if (withinHtml && getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer).toLowerCase().equals("/script")) {
            return;
        }
        if (withinHtml) {
            writer.indentIncrease();
        }
        boolean needsNewLine = withinHtml;
        boolean needsLineBefore = false;
        boolean inInlineBrace = false;
        boolean inBracket = false;
        int charBeforeBlockComment = -1;
        int cur = -1;
        int prev = ' ';
        int prevNonWhitespace = -1;
        boolean needsWhitespace = false;
        boolean isOnBlankLine = false;
        boolean hadCdataBlock = false;
        while ((cur = reader.read()) != -1) {
            if (cur == '<' && "![CDATA[".equals(reader.readAheadSkipAllWhitespace(8))) {
                writer.newLineMaybe();
                writer.write("<![CDATA[");
                writer.newLine();
                reader.skipAllWhitespace(8);
                hadCdataBlock = true;
                needsNewLine = false;
                continue;
            }
            if (hadCdataBlock && cur == ']' && "]>".equals(reader.readAheadSkipAllWhitespace(2))) {
                writer.newLineMaybe();
                writer.write("]]>");
                reader.skipAllWhitespace(2);
                needsNewLine = false;
                hadCdataBlock = false;
                continue;
            }
            if (withinHtml && cur == '<') {
                reader.unread(cur);
                if (getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer).toLowerCase().equals("/script")) {
                    writer.indentDecrease();
                    if (!needsNewLine) {
                        writer.newLineMaybe();
                    }
                    return;
                }
                reader.read();
            }
            if (cur == '/' && reader.readAhead() == '/') {
                if (!isOnBlankLine && (prevNonWhitespace == ';')) {
                    writer.write(' ');
                    needsWhitespace = false;
                }
                if (prevNonWhitespace == '{') {
                    writer.newLine();
                    writer.indentIncrease();
                } else if (prevNonWhitespace == -1 && !withinHtml) {
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
                jumpOverInlineComment(reader, writer, true);
                needsLineBefore = true;
                prevNonWhitespace = -3;
                inInlineBrace = false;
                continue;
            }
            if (cur == '/' && reader.readAhead() == '*') {
                if (prevNonWhitespace == '{') {
                    writer.newLine();
                    writer.indentIncrease();
                } else if (!isOnBlankLine && prevNonWhitespace != '(' && prevNonWhitespace != -1 && prevNonWhitespace != -3) {
                    writer.write(' ');
                } else if (prevNonWhitespace == -1 && !withinHtml) {
                } else if (prevNonWhitespace == ';' || prevNonWhitespace == -1 || prevNonWhitespace == -2 || prevNonWhitespace == -1 || prevNonWhitespace == '}') {
                    writer.newLine();
                }
                writer.write(cur);
                writer.write(reader.read());
                jumpOverBlockComment(reader, writer, true);
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
                if (needsNewLine) {
                    writer.newLineMaybe();
                    needsNewLine = false;
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
                    if (cur == ',' || cur == ')' || cur == ';') {
                    } else if (!isNextWordInlineBrace(reader, writer)) {
                        writer.newLine();
                    } else {
                        writer.write(' ');
                        inInlineBrace = true;
                    }
                } else if (prevNonWhitespace == ']' && (cur == ')')) {
                } else if (prevNonWhitespace != -4 && needsWhitespaceBetweenJavascript(reader, writer, prevNonWhitespace, cur)) {
                    writer.write(' ');
                    needsWhitespace = false;
                } else if (needsWhitespace) {
                    if (!doesntActuallyNeedWhitespaceBeforeJavascript(reader, writer, cur) && prevNonWhitespace != -3) {
                        if (writer.getPrevious() != '\n') {
                            writer.write(' ');
                        }
                    }
                    needsWhitespace = false;
                }
                if (cur == '}') {
                    writer.indentDecrease();
                    writer.newLineMaybe();
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
                boolean didRegexpMode = false;
                if (cur == '"') {
                    jumpOverString(reader, writer, true);
                } else if (cur == '\'') {
                    jumpOverSingleString(reader, writer, true);
                } else if (cur == '/' && (prevNonWhitespace == '=' || prevNonWhitespace == '(' || prevNonWhitespace == '.' || prevNonWhitespace == ':')) {
                    jumpOverJavascriptRegexp(reader, writer, true);
                    prevNonWhitespace = -4;
                    needsWhitespace = false;
                    didRegexpMode = true;
                }
                if (!Character.isWhitespace(cur) && !didRegexpMode) {
                    prevNonWhitespace = cur;
                }
            }
            prev = cur;
            if (withinHtml) {
                String nextTag = getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer);
                if (nextTag != null && "/script".equals(nextTag.toLowerCase())) {
                    writer.indentDecrease();
                    if (!needsNewLine) {
                        writer.newLineMaybe();
                    }
                    return;
                }
            }
            if (reader.readAhead(5) != null && reader.readAhead(5).equals("<?php")) {
                if (isOnBlankLine) {
                    writer.newLineMaybe();
                }
                if (prevNonWhitespace == '{') {
                    writer.indentIncrease();
                }
                getInline().cleanPhpBlock(reader, writer);
                if (prevNonWhitespace == '{') {
                    writer.indentDecrease();
                }
            }
        }
        if (withinHtml) {
            throw new InlineCleanerException("Unexpectedly terminated out of Javascript mode", reader);
        } else {
            return;
        }
    }
