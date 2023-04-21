    public void cleanHtmlCss(InlineStringReader reader, InlineStringWriter writer, boolean withinHtml) throws IOException, CleanerException {
        if (withinHtml && getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer).toLowerCase().equals("/style")) {
            return;
        }
        if (withinHtml) {
            writer.indentIncrease();
        }
        boolean needsNewLine = withinHtml;
        boolean needsLineBefore = false;
        int charBeforeBlockComment = -1;
        int cur = -1;
        int prev = ' ';
        int prevNonWhitespace = -1;
        boolean needsWhitespace = false;
        boolean isOnBlankLine = false;
        boolean doingDot = false;
        int braceCount = 0;
        while ((cur = reader.read()) != -1) {
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
                if (prev != '[') {
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
                if (cur == '.' && isCssIdentifierCharacter(prevNonWhitespace) && !Character.isWhitespace(prev)) {
                    doingDot = true;
                }
                isOnBlankLine = false;
                if (prevNonWhitespace == ';') {
                    writer.newLine();
                } else if (prevNonWhitespace == -2 && cur != ';' && cur != ',' && cur != ')' && cur != '{' && !Character.isWhitespace(cur) && charBeforeBlockComment != '(') {
                    writer.newLineMaybe();
                    needsWhitespace = false;
                } else if (prevNonWhitespace == '{') {
                    writer.newLineMaybe();
                    writer.indentIncrease();
                    needsWhitespace = false;
                } else if (prevNonWhitespace == '}') {
                    writer.newLine();
                } else if (prevNonWhitespace == '.' && doingDot) {
                    doingDot = false;
                } else if (prevNonWhitespace == ':' && braceCount == 0) {
                } else if (!doingDot && needsWhitespaceBetweenCss(reader, writer, prevNonWhitespace, cur)) {
                    writer.write(' ');
                    needsWhitespace = false;
                } else if (needsWhitespace) {
                    if (!doesntActuallyNeedWhitespaceBeforeCss(reader, writer, cur) && prevNonWhitespace != -3) {
                        if (writer.getPrevious() != '\n') {
                            writer.write(' ');
                        }
                    }
                    needsWhitespace = false;
                }
                if (cur == '}') {
                    writer.indentDecrease();
                    writer.newLineMaybe();
                    braceCount--;
                } else if (cur == '{') {
                    braceCount++;
                }
                writer.write(cur);
                if (cur == '"') {
                    jumpOverString(reader, writer, true);
                } else if (cur == '\'') {
                    jumpOverSingleString(reader, writer, true);
                }
                if (!Character.isWhitespace(cur)) {
                    prevNonWhitespace = cur;
                }
            }
            prev = cur;
            if (withinHtml) {
                String nextTag = getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer);
                if (nextTag.toLowerCase().equals("/style")) {
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
            throw new InlineCleanerException("Unexpectedly terminated out of CSS mode", reader);
        } else {
            return;
        }
    }
