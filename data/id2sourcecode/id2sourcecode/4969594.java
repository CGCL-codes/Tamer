    public double[] getSextPVs() {
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("SEXT_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("SEXT_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("SEXT_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("SEXT_4_mag:Field");
        q_get[0] = getValue(ch_1);
        q_get[1] = getValue(ch_2);
        q_get[2] = getValue(ch_3);
        q_get[3] = getValue(ch_4);
        return q_get;
    }
