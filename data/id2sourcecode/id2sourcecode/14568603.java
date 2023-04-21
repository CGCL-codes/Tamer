    public void initScalePVs() {
        dtlscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("DTL_Diag:CalcL_BLM:SumCur"));
        cclscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("CCL_Diag:CalcL_BLM:SumCur"));
        sclscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("SCL_Diag:CalcL_BLM:SumCur"));
        ringscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Diag:CalcL_BLM:SumCur"));
        hebtscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("HEBT_Diag:CalcL_BLM:SumCur"));
        rtbtscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("RTBT_Diag:CalcL_BLM:SumCur"));
        ldmpscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("LDmp_Diag:CalcL_BLM:SumCur"));
        idmpscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("IDmp_Diag:CalcL_BLM:SumCur"));
        edmpscaleagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("EDmp_Diag:CalcL_BLM:SumCur"));
        dtlscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                dtlscalefac = value;
            }
        });
        cclscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                cclscalefac = value;
            }
        });
        sclscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                sclscalefac = value;
            }
        });
        hebtscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                hebtscalefac = value;
            }
        });
        ringscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ringscalefac = value;
            }
        });
        rtbtscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                rtbtscalefac = value;
            }
        });
        ldmpscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ldmpscalefac = value;
            }
        });
        idmpscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                idmpscalefac = value;
            }
        });
        edmpscaleagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                edmpscalefac = value;
            }
        });
    }
