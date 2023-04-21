    public void setQuadPVs(double q_set[]) {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("QUAD_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("QUAD_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("QUAD_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("QUAD_4_mag:Field");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("QUAD_5_mag:Field");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("QUAD_6_mag:Field");
        setValue(ch_1, q_set[0]);
        setValue(ch_2, q_set[1]);
        setValue(ch_3, q_set[2]);
        setValue(ch_4, q_set[3]);
        setValue(ch_5, q_set[4]);
        setValue(ch_6, q_set[5]);
    }
