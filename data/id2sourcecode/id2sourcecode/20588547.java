    public void clearRange(Span clearSpan, int mode, Object source, AbstractCompoundEdit ce, boolean[] trackMap, BlendContext bc) throws IOException {
        if (trackMap.length != this.getChannelNum()) throw new IllegalArgumentException(trackMap.toString());
        if (trackMap.length == 0) return;
        switch(mode) {
            case MODE_INSERT:
                clearRangeIns(clearSpan, mode, source, ce, trackMap, bc);
                break;
            case MODE_OVERWRITE:
                clearRangeOvr(clearSpan, mode, source, ce, trackMap, bc);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(mode));
        }
    }
