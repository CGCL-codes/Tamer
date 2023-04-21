    final void lookupswitch(int val) {
        if (traceByteCodes) System.out.println("lookupswitch");
        int start = bindex - 1;
        int align = bindex & 3;
        if (align != 0) bindex += 4 - align;
        int defaultoff = this.fetch4BytesSigned();
        int npairs = this.fetch4BytesSigned();
        int first = 0;
        int last = npairs - 1;
        for (; ; ) {
            if (first > last) {
                bindex = start + defaultoff;
                break;
            }
            int current = (last + first) / 2;
            int match = this.fetch4BytesSigned(bindex + current * 8);
            if (val < match) {
                last = current - 1;
            } else if (val > match) {
                first = current + 1;
            } else {
                int offset = this.fetch4BytesSigned(bindex + 4 + current * 8);
                bindex = start + offset;
                break;
            }
        }
    }
