    public void initMPSPVs() {
        dtlmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("DTL_Diag:BLM:Alarm"));
        cclmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("CCL_Diag:BLM:Alarm"));
        sclmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("SCL_Diag:BLM:Alarm"));
        ringmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Diag:BLM_LB:Alarm"));
        hebtmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("HEBT_Diag:BLM:Alarm"));
        rtbtmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("RTBT_Diag:BLM_ALarm"));
        ldmpmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("LDmp_Diag:BLM:Alarm"));
        idmpmpsagent = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("IDmp_Diag:BLM:Alarm"));
        dtlmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                dtlseverity = severity;
            }
        });
        cclmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                cclseverity = severity;
            }
        });
        sclmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                sclseverity = severity;
            }
        });
        hebtmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                hebtseverity = severity;
            }
        });
        ringmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ringseverity = severity;
            }
        });
        rtbtmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                rtbtseverity = severity;
            }
        });
        ldmpmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                ldmpseverity = severity;
            }
        });
        idmpmpsagent.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value, int severity) {
                idmpseverity = severity;
            }
        });
    }
