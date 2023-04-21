    public double[] getQuadLLimitPVs() {
        double limit[] = new double[6];
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("QUAD_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("QUAD_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("QUAD_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("QUAD_4_mag:Field");
        Channel ch_5 = ChannelFactory.defaultFactory().getChannel("QUAD_5_mag:Field");
        Channel ch_6 = ChannelFactory.defaultFactory().getChannel("QUAD_6_mag:Field");
        limit[0] = getMagLowLimits(ch_1);
        limit[1] = getMagLowLimits(ch_2);
        limit[2] = getMagLowLimits(ch_3);
        limit[3] = getMagLowLimits(ch_4);
        limit[4] = getMagLowLimits(ch_5);
        limit[5] = getMagLowLimits(ch_6);
        return limit;
    }
