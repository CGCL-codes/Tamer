    public void addBufferReadMessages(OSCBundle bndl, Span readSpan, Buffer[] bufs, int bufOff) {
        final int len = (int) readSpan.getLength();
        if (len == 0) return;
        final long fOffset = fileSpan.start + readSpan.start - span.start;
        if ((fOffset < fileSpan.start) || ((fOffset + len) > fileSpan.stop)) {
            throw new IllegalArgumentException(fOffset + " ... " + (fOffset + len) + " not within " + fileSpan.toString());
        }
        if ((bufs.length != 1) || (bufs[0].getNumChannels() != f.getChannelNum())) {
            throw new IllegalArgumentException("Wrong # of buffers / channels (required: 1 / " + f.getChannelNum());
        }
        bndl.addPacket(bufs[0].readMsg(fileName, fOffset, len, bufOff));
    }
