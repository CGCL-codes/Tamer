    protected void blockUpdate(byte[] buf, int pos, int len) {
        tiger.reset();
        tiger.update((byte) 0);
        tiger.update(buf, pos, len);
        if ((len == 0) && (nodes.size() > 0)) return;
        byte[] digest = tiger.digest();
        push(digest);
    }
