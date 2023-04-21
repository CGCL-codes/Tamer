    public double[] getSwitchBmMdPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:10uSec.RVAL");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:50uSec.RVAL");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:100uSec.RVAL");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:1mSec.RVAL");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:FullPwr.RVAL");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:Off.RVAL");
        Channel ch_7 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:StandBy.RVAL");
        Channel ch_8 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_BmMd:MPSTest.RVAL");
        SwitchBmMd_get[0] = getValue(ch_1);
        SwitchBmMd_get[1] = getValue(ch_2);
        SwitchBmMd_get[2] = getValue(ch_3);
        SwitchBmMd_get[3] = getValue(ch_4);
        SwitchBmMd_get[4] = getValue(ch_5);
        SwitchBmMd_get[5] = getValue(ch_6);
        SwitchBmMd_get[6] = getValue(ch_7);
        SwitchBmMd_get[7] = getValue(ch_8);
        return SwitchBmMd_get;
    }
