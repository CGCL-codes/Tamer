    public double[] getQuadULimitPVs() {
        double limit[] = new double[6];
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("QUAD_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("QUAD_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("QUAD_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("QUAD_4_mag:Field");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("QUAD_5_mag:Field");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("QUAD_6_mag:Field");
        limit[0] = getMagUpLimits(ch_1);
        limit[1] = getMagUpLimits(ch_2);
        limit[2] = getMagUpLimits(ch_3);
        limit[3] = getMagUpLimits(ch_4);
        limit[4] = getMagUpLimits(ch_5);
        limit[5] = getMagUpLimits(ch_6);
        return limit;
    }
