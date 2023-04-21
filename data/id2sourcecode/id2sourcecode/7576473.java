    public void addBufferReadMessages(OSCBundle bndl, Span readSpan, Buffer[] bufs, int bufOff) {
        final int len = (int) readSpan.getLength();
        if (len == 0) return;
        long fOffset;
        if (bufs.length != fs.length) {
            throw new IllegalArgumentException("Wrong # of buffers (" + bufs.length + " != " + fs.length + ")");
        }
        for (int i = 0; i < fs.length; i++) {
            fOffset = fileSpans[i].start + readSpan.start - span.start;
            if ((fOffset < fileSpans[i].start) || ((fOffset + len) > fileSpans[i].stop)) {
                throw new IllegalArgumentException(fOffset + " ... " + (fOffset + len) + " not within " + fileSpans[i].toString());
            }
            if ((bufs[i].getNumChannels() != fs[i].getChannelNum())) {
                throw new IllegalArgumentException("Channel mismatch (" + bufs[i].getNumChannels() + " != " + fs[i].getChannelNum());
            }
            bndl.addPacket(bufs[i].readMsg(fileNames[i], fOffset, len, bufOff));
        }
    }
