    public void initSumPVs() {
        dtlsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("DTL_Diag:CalcL_BLM:Sum"));
        cclsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("CCL_Diag:CalcL_BLM:Sum"));
        sclsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("SCL_Diag:CalcL_BLM:Sum"));
        ringsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Diag:CalcL_BLM:Sum"));
        hebtsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("HEBT_Diag:CalcL_BLM:Sum"));
        rtbtsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("RTBT_Diag:CalcL_BLM:Sum"));
        ldmpsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("LDmp_Diag:CalcL_BLM:Sum"));
        idmpsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("IDmp_Diag:CalcL_BLM:Sum"));
        edmpsumagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("EDmp_Diag:CalcL_BLM:Sum"));
        dtlsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                dtlsum = value;
            }
        });
        cclsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                cclsum = value;
            }
        });
        sclsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                sclsum = value;
            }
        });
        hebtsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                hebtsum = value;
            }
        });
        ringsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ringsum = value;
            }
        });
        rtbtsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                rtbtsum = value;
            }
        });
        ldmpsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ldmpsum = value;
            }
        });
        idmpsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                idmpsum = value;
            }
        });
        edmpsumagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                edmpsum = value;
            }
        });
    }
