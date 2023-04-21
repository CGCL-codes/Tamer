    public void visit(ArrayRead arrayRead) {
        if (_lim.db) writeln("Arrayread: " + arrayRead.toString());
        genericBlockVisit(arrayRead);
    }
