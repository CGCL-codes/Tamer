    public double[] getRTDLBmMdPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:10uSec.RVAL");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:50uSec.RVAL");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:100uSec.RVAL");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:1mSec.RVAL");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:FullPwr.RVAL");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:Off.RVAL");
        Channel ch_7 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:StandBy.RVAL");
        Channel ch_8 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_BmMd:MPSTest.RVAL");
        RTDLBmMd_get[0] = getValue(ch_1);
        RTDLBmMd_get[1] = getValue(ch_2);
        RTDLBmMd_get[2] = getValue(ch_3);
        RTDLBmMd_get[3] = getValue(ch_4);
        RTDLBmMd_get[4] = getValue(ch_5);
        RTDLBmMd_get[5] = getValue(ch_6);
        RTDLBmMd_get[6] = getValue(ch_7);
        RTDLBmMd_get[7] = getValue(ch_8);
        return RTDLBmMd_get;
    }
