    public static InkAffineMapping createIdentityInkAffinMapping(InkInk ink, InkTraceFormat sourceFormat, InkTraceFormat targetFormat) {
        InkAffineMapping map = new InkAffineMapping(ink);
        int sCount = 0;
        int tCount = 0;
        for (InkChannel c : targetFormat.getChannels()) {
            tCount++;
            InkBind bt = new InkBind();
            bt.target = c.getName();
            map.addBind(bt);
            if (sourceFormat.containsChannel(c.getName())) {
                sCount++;
                InkBind b = new InkBind();
                b.source = c.getName();
                map.addBind(b);
            }
        }
        InkMatrix m = new InkMatrix(ink);
        double[][] fm = new double[tCount][sCount];
        double[] tr = new double[tCount];
        for (InkChannel c : targetFormat.getChannels()) {
            if (sourceFormat.containsChannel(c.getName())) {
                fm[map.getTargetIndex(targetFormat, c.getName())][map.getSourcetIndex(sourceFormat, c.getName())] = 1;
            } else {
                tr[map.getTargetIndex(targetFormat, c.getName())] = Double.NaN;
            }
        }
        m.setMatrix(fm, tr);
        map.matrix = m;
        return map;
    }
