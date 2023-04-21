    public void print(Consumer out) {
        if (this == empty) {
            out.write("#!void");
            return;
        }
        Object[] vals = toArray();
        int size = vals.length;
        boolean readable = true;
        if (readable) out.write("#<values");
        for (int i = 0; ; ) {
            int next = nextDataIndex(i);
            if (next < 0) break;
            out.write(' ');
            if (i >= gapEnd) i -= gapEnd - gapStart;
            Object val = getPosNext(i << 1);
            if (val instanceof Printable) ((Printable) val).print(out); else out.writeObject(val);
            i = next;
        }
        if (readable) out.write('>');
    }
