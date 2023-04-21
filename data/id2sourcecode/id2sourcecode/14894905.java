    public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet) throws BadLocationException {
        int next = super.getNextVisualPositionFrom(pos, b, a, direction, biasRet);
        if (!isVisible()) return next;
        int start = getStartOffset();
        int end = getEndOffset();
        if (next < start || end < next) return next;
        if (next == start) return next;
        if (direction == WEST) return start;
        if (direction == EAST) {
            biasRet[0] = Position.Bias.Backward;
            if (pos == end && next == end && b == b.Backward) return end;
            return end;
        }
        int mid = (start + end) / 2;
        next = (next <= mid) ? start : end;
        return next;
    }
