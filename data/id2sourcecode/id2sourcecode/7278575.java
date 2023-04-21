    protected final double[] getChannelInverse(RGBBase.Channel channel, double channelValue) {
        double r = channel == RGBBase.Channel.R ? channelValue : 0;
        double g = channel == RGBBase.Channel.G ? channelValue : 0;
        double b = channel == RGBBase.Channel.B ? channelValue : 0;
        double[] XYZValues = adapter.getXYZ(r, g, b).getValues();
        if (XYZValues[0] == 0 && XYZValues[1] == 0 && XYZValues[2] == 0) {
            return null;
        } else {
            return DoubleArray.transpose(DoubleArray.pseudoInverse(new double[][] { XYZValues }))[0];
        }
    }
