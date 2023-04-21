    public double[] getSwitchMachMdPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:MEBT_BS.RVAL");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:CCL_BS.RVAL");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:LinDmp.RVAL");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:InjDmp.RVAL");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:Ring.RVAL");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:ExtDmp.RVAL");
        Channel ch_7 = ChannelFactory.defaultFactory().getChannel("ICS_MPS:Switch_MachMd:Tgt.RVAL");
        SwitchMachMd_get[0] = getValue(ch_1);
        SwitchMachMd_get[1] = getValue(ch_2);
        SwitchMachMd_get[2] = getValue(ch_3);
        SwitchMachMd_get[3] = getValue(ch_4);
        SwitchMachMd_get[4] = getValue(ch_5);
        SwitchMachMd_get[5] = getValue(ch_6);
        SwitchMachMd_get[6] = getValue(ch_7);
        return SwitchMachMd_get;
    }
