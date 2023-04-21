    private final TermVectorOffsetInfo checkValue(TermVectorOffsetInfo currentVector, Iterator<TermVectorOffsetInfo> vectorIterator, int startOffset, Fragment fragment) {
        if (currentVector == null) return null;
        StringBuffer result = new StringBuffer();
        String originalText = fragment.getOriginalText();
        int originalTextLength = originalText.length();
        int endOffset = startOffset + originalTextLength;
        int pos = 0;
        while (currentVector != null) {
            int end = currentVector.getEndOffset() - fragment.vectorOffset;
            if (end > endOffset) break;
            int start = currentVector.getStartOffset() - fragment.vectorOffset;
            if (start >= startOffset) {
                appendSubString(originalText, pos, start - startOffset, result);
                if (tags != null) result.append(tags[0]);
                appendSubString(originalText, start - startOffset, end - startOffset, result);
                if (tags != null) result.append(tags[1]);
                pos = end - startOffset;
            }
            currentVector = vectorIterator.hasNext() ? vectorIterator.next() : null;
        }
        if (result.length() == 0) return currentVector;
        if (pos < originalTextLength) appendSubString(originalText, pos, originalTextLength, result);
        fragment.setHighlightedText(result.toString());
        return currentVector;
    }
