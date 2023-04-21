    public void setSextPVs(double q_set[]) {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("SEXT_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("SEXT_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("SEXT_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("SEXT_4_mag:Field");
        setValue(ch_1, q_set[0]);
        setValue(ch_2, q_set[1]);
        setValue(ch_3, q_set[2]);
        setValue(ch_4, q_set[3]);
    }
