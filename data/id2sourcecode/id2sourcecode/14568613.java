    public void initAvgPVs() {
        dtlavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("DTL_Diag:CalcL_BLM:Avg"));
        cclavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("CCL_Diag:CalcL_BLM:Avg"));
        sclavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("SCL_Diag:CalcL_BLM:Avg"));
        ringavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Diag:CalcL_BLM:Avg"));
        hebtavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("HEBT_Diag:CalcL_BLM:Avg"));
        rtbtavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("RTBT_Diag:CalcL_BLM:Avg"));
        ldmpavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("LDmp_Diag:CalcL_BLM:Avg"));
        idmpavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("IDmp_Diag:CalcL_BLM:Avg"));
        edmpavgagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("EDmp_Diag:CalcL_BLM:Avg"));
        dtlavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                dtlavg = value;
            }
        });
        cclavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                cclavg = value;
            }
        });
        sclavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                sclavg = value;
            }
        });
        hebtavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                hebtavg = value;
            }
        });
        ringavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ringavg = value;
            }
        });
        rtbtavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                rtbtavg = value;
            }
        });
        ldmpavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ldmpavg = value;
            }
        });
        idmpavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                idmpavg = value;
            }
        });
        edmpavgagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                idmpavg = value;
            }
        });
    }
