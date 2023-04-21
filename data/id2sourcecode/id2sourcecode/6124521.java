    public byte[] getFirstDigest() throws NoMatchingContentFoundException, IOException {
        if (null == _firstSegment) {
            ContentObject firstSegment = getFirstSegment();
            setFirstSegment(firstSegment);
        }
        return _firstSegment.digest();
    }
