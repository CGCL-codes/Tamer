    public LineInfo getLineInfo(int lineNumber) {
        if (noLineInfo) {
            return (null);
        }
        LineInfo li = new LineInfo(getLineAt(lineNumber));
        li.inComment = false;
        li.inLiteral = false;
        li.keyCt = 0;
        int firstToken = -1;
        int lastToken = -1;
        int leftscan = 0;
        int rightscan = numberTokens - 1;
        while ((firstToken == -1) && (lastToken == -1)) {
            int scan = (leftscan + rightscan) / 2;
            int lno = getLineNumber(scan);
            if (lno > lineNumber) {
                if (rightscan == scan) {
                    return (li);
                }
                rightscan = scan;
            } else if (lno < lineNumber) {
                if (leftscan == scan) {
                    return (li);
                }
                leftscan = scan;
            } else {
                while (lno == lineNumber) {
                    firstToken = scan;
                    if (scan == 0) {
                        break;
                    }
                    scan--;
                    lno = getLineNumber(scan);
                }
                scan = firstToken;
                lno = lineNumber;
                while (lno == lineNumber) {
                    lastToken = scan;
                    scan++;
                    if (scan == numberTokens) {
                        break;
                    }
                    lno = getLineNumber(scan);
                }
            }
        }
        li.inComment = false;
        li.inLiteral = false;
        li.keyCt = lastToken - firstToken + 1;
        if (firstToken == -1) {
            li.keyCt = 0;
        } else {
            li.keyStarts = new short[li.keyCt];
            li.keyEnds = new short[li.keyCt];
            li.keyTypes = new byte[li.keyCt];
            if (firstToken != -1) {
                for (int i = firstToken; i <= lastToken; i++) {
                    li.keyStarts[i - firstToken] = (short) getLineOffset(i);
                    int size = getSize(i);
                    li.keyEnds[i - firstToken] = (short) (getSize(i) + li.keyStarts[i - firstToken]);
                    li.keyTypes[i - firstToken] = getEditType(i);
                }
            }
        }
        return (li);
    }
