    public int[] getMPSHPRFMPSpvs(final String initPvStr, final String orgPvStr) {
        String[] chNames = { "", "", "", "", "" };
        MebtMap = new LinkedHashMap();
        String chName = orgPvStr + ":Sts_Switch";
        chNames[0] = chName;
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel(chName);
        chName = orgPvStr + ":Rdy_Fil";
        chNames[1] = chName;
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel(chName);
        chName = orgPvStr + ":Rdy_HV";
        chNames[2] = chName;
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel(chName);
        chName = orgPvStr + ":FPAR_MEBT_BS_chan_status";
        chNames[3] = chName;
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel(chName);
        chName = orgPvStr + ":FPAR_MEBT_BS_cable_status";
        chNames[4] = chName;
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel(chName);
        MPSHPRFCheckMPSpvs_get[0] = getValue(ch_1);
        MPSHPRFCheckMPSpvs_get[1] = getValue(ch_2);
        MPSHPRFCheckMPSpvs_get[2] = getValue(ch_3);
        MPSHPRFCheckMPSpvs_get[3] = getValue(ch_4);
        MPSHPRFCheckMPSpvs_get[4] = getValue(ch_5);
        String str;
        for (int i = 0; i < 5; i++) {
            str = "" + MPSHPRFCheckMPSpvs_get[i];
            MebtMap.put(chNames[i], str);
        }
        return MPSHPRFCheckMPSpvs_get;
    }
