    protected int getTokenIndexForNodeIndentifier(Identifier xdmId, boolean onlyWhenFound, TokenInterface peerToMatch) {
        int lPos = 0;
        int rPos = tokenI - 1;
        int midPos;
        do {
            midPos = (lPos + rPos) / 2;
            Identifier mid = tokenBuffer[midPos].getNodeId();
            while (mid == null && midPos > lPos + 1) {
                midPos = midPos - 1;
                mid = tokenBuffer[midPos].getNodeId();
            }
            while (mid == null && midPos < rPos - 1) {
                midPos = midPos + 1;
                mid = tokenBuffer[midPos].getNodeId();
            }
            if (mid == null) break;
            int cmpRes = xdmId.compare(mid);
            switch(cmpRes) {
                case 0:
                    return midPos;
                case 1:
                    rPos = midPos;
                    break;
                case -1:
                    lPos = midPos;
                    break;
                default:
                    return -1;
            }
        } while (rPos - lPos > 1);
        if (xdmId.compare(tokenBuffer[lPos].getNodeId()) == 0) return lPos;
        if (xdmId.compare(tokenBuffer[rPos].getNodeId()) == 0) return rPos;
        if (onlyWhenFound) return -1; else for (int curPos = lPos; curPos <= rPos; curPos++) {
            if (peerToMatch != null) {
                if (peerToMatch.getEventType() == Type.START_DOCUMENT && tokenBuffer[curPos].getEventType() == Type.END_DOCUMENT) return curPos;
                if (peerToMatch.getEventType() == Type.START_TAG && tokenBuffer[curPos].getEventType() == Type.END_TAG) {
                    NamedToken tokStart = (NamedToken) peerToMatch;
                    NamedToken tokEnd = (NamedToken) tokenBuffer[curPos];
                    if (tokEnd.getName().equals(tokStart.getName()) && ((tokEnd.getNS() == null && tokStart.getNS() == null) || (tokEnd.getNS() != null && tokEnd.getNS().equals(tokStart.getNS())))) return curPos;
                }
                if (peerToMatch.getEventType() != Type.START_DOCUMENT && peerToMatch.getEventType() != Type.START_TAG) return curPos;
            }
            if (xdmId.compare(tokenBuffer[curPos].getNodeId()) == 1) return curPos - 1;
        }
        return rPos;
    }
