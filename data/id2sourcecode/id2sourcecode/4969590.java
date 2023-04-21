    public double[] getQuadPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("QUAD_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("QUAD_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("QUAD_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("QUAD_4_mag:Field");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("QUAD_5_mag:Field");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("QUAD_6_mag:Field");
        q_get[0] = getValue(ch_1);
        q_get[1] = getValue(ch_2);
        q_get[2] = getValue(ch_3);
        q_get[3] = getValue(ch_4);
        q_get[4] = getValue(ch_5);
        q_get[5] = getValue(ch_6);
        return q_get;
    }
