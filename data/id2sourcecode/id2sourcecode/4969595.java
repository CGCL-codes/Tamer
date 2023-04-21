    public double[] getSextLLimitPVs() {
        double limit[] = new double[4];
        Channel ch_1 = ChannelFactory.defaultFactory().getChannel("SEXT_1_mag:Field");
        Channel ch_2 = ChannelFactory.defaultFactory().getChannel("SEXT_2_mag:Field");
        Channel ch_3 = ChannelFactory.defaultFactory().getChannel("SEXT_3_mag:Field");
        Channel ch_4 = ChannelFactory.defaultFactory().getChannel("SEXT_4_mag:Field");
        limit[0] = getMagLowLimits(ch_1);
        limit[1] = getMagLowLimits(ch_2);
        limit[2] = getMagLowLimits(ch_3);
        limit[3] = getMagLowLimits(ch_4);
        return limit;
    }
