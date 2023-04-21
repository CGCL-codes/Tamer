    public void fillBlock() throws LimitReachedException {
        IMessageDigest mdc = (IMessageDigest) md.clone();
        buffer = mdc.digest();
        md.update(buffer, 0, buffer.length);
    }
