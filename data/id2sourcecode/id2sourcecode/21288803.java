    public void testMapCreation() {
        ChannelMapOperationFactory f = new ChannelMapOperationFactory();
        ColorCurve r = new ColorCurve();
        r.addPoint(0.0, 0.1);
        r.addPoint(0.2, 0.4);
        r.addPoint(1.0, 1.0);
        f.setChannelCurve("red", r);
        ColorCurve b = new ColorCurve();
        b.addPoint(0.0, 0.2);
        b.addPoint(0.4, 0.4);
        b.addPoint(1.0, 0.9);
        f.setChannelCurve("blue", b);
        ChannelMapOperation o = f.create();
        ColorCurve r1 = o.getChannelCurve("red");
        assertEquals(r, r1);
        ColorCurve b1 = o.getChannelCurve("blue");
        assertEquals(b, b1);
        ColorCurve e1 = o.getChannelCurve("nonexisting");
        assertNull(e1);
        ChannelMapOperationFactory f2 = new ChannelMapOperationFactory(o);
        ChannelMapOperation o2 = f2.create();
        assertEquals(o, o2);
        ColorCurve r2 = new ColorCurve();
        f2.setChannelCurve("red", r2);
        ChannelMapOperation o3 = f2.create();
        assertFalse(o2.equals(o3));
    }
