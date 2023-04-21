    public double[] getRTDLMachMdPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:MEBT_BS.RVAL");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:CCL_BS.RVAL");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:LinDmp.RVAL");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:InjDmp.RVAL");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:Ring.RVAL");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:ExtDmp.RVAL");
        Channel ch_7 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:RTDL_MachMd:Tgt.RVAL");
        RTDLMachMd_get[0] = getValue(ch_1);
        RTDLMachMd_get[1] = getValue(ch_2);
        RTDLMachMd_get[2] = getValue(ch_3);
        RTDLMachMd_get[3] = getValue(ch_4);
        RTDLMachMd_get[4] = getValue(ch_5);
        RTDLMachMd_get[5] = getValue(ch_6);
        RTDLMachMd_get[6] = getValue(ch_7);
        return RTDLMachMd_get;
    }
