    public OutputStream getOutputStream() throws IPCException {
        if (null != myDirection && myDirection != Directions.Writer) {
            String msg = "Attempt to change direction from reader to writer";
            throw new IPCException(msg);
        }
        if (null == mySegment) {
            throw new IPCException("Not connected to a segment");
        }
        reserveWriter();
        return new SMFIFOOutputStream(this, createSBSB());
    }
