    @Test
    public void testApcColumnIsNull() {
        FmsData fmsData = FmsData.getInstance();
        String jobname = "cbt_com_cn_12977";
        System.out.println("Jobname : " + jobname);
        FmsChannel chnl = fmsData.getChannel(jobname);
        System.out.println("Channel id : " + chnl.getId());
        System.out.println("Channel name : " + chnl.getName());
        FmsSource source = fmsData.getSource(chnl.getSourceId());
        System.out.println("Source id : " + source.getId());
        System.out.println("Source name : " + source.getName());
        System.out.println("Source big kind : " + source.getBigKind());
        System.out.println("Source small kind : " + source.getSmallKind());
        System.out.println("Source url : " + source.getUrl());
        System.out.println("Source core : " + source.getCore());
        System.out.println("Source expertise : " + source.getSourceExpertise());
        System.out.println("Source influence : " + source.getSourceInfluence());
        System.out.println("Source originality : " + source.getSourceOriginality());
        assert (null == chnl.getApcColumnId());
    }
