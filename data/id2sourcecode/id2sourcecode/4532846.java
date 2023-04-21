    public void visit(AbsoluteMemoryRead read) {
        if (_lim.db) writeln("AbsoluteMemoryRead: " + read.toString());
        genericBlockVisit(read);
    }
