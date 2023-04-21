    private byte[] collapse() {
        byte[] last = null;
        for (int i = 0; i < nodes.size(); i++) {
            byte[] current = nodes.get(i);
            if (current == MARKER) continue;
            if (last == null) last = current; else {
                tiger.reset();
                tiger.update((byte) 1);
                tiger.update(current);
                tiger.update(last);
                last = tiger.digest();
            }
            nodes.set(i, MARKER);
        }
        Assert.that(last != null);
        return last;
    }
