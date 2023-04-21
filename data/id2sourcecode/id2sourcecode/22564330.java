    public void setUp() throws IOException {
        readFrom = new PermanentSpace(TestImpl.zms);
        Dim vdim = (Dim) readFrom.getVStreamDim();
        Cell h = readFrom.getHomeCell();
        c1 = h.N(h);
        c2 = c1.N(h);
        cspan = readFrom.makeSpanRank("blaah", c1);
        c2.connect(h, cspan);
        Mediaserver.Id id = readFrom.save(TestImpl.zms);
        readFrom = new PermanentSpace(TestImpl.zms, id);
        h = readFrom.getHomeCell();
        c1 = h.s(h);
        c2 = c1.s(h);
        cspan = c2.s(h);
        writeTo = new PermanentSpace(TestImpl.zms);
        ugly = new GZZ1Ugliness(readFrom, writeTo);
    }
