    public InputStream getInputStream() throws IPCException {
        if (null != myDirection && myDirection != Directions.Reader) {
            String msg = "Attempt to change direction from writer to reader";
            throw new IPCException(msg);
        }
        if (null == mySegment) {
            throw new IPCException("Not connected to a segment");
        }
        reserveReader();
        SMFIFOInputStream istream = new SMFIFOInputStream(this);
        return istream;
    }
