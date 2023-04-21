    public byte[] getFirstDigest() {
        ContentObject firstSegment = _segmenter.getFirstSegment();
        if (null != firstSegment) {
            return firstSegment.digest();
        } else {
            return null;
        }
    }
