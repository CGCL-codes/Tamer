    public void finish() {
        if (byteCount != 0) {
            digestList.add(new Buffer(md.digest()));
        }
        digests = (Buffer[]) digestList.toArray(new Buffer[0]);
        digestList = null;
    }
